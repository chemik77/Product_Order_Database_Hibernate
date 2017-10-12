package pl.chemik77.model;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.chemik77.model.Customer;
import pl.chemik77.model.UserAcc;

@RunWith(Arquillian.class)
public class UserAccTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(UserAcc.class.getPackage())
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@PersistenceContext(unitName = "test")
	EntityManager em;

	@Inject
	UserTransaction utx;

	private final String SQL_USERACC = "SELECT u FROM UserAcc u ORDER BY u.id";
	private final String SQL_CUSTOMER = "SELECT c FROM Customer c ORDER BY c.id";

	private List<UserAcc> expectedUsers;
	private List<UserAcc> actualUsers;
	private List<Customer> actualCustomers;

	@Before
	public void prepareTest() throws Exception {
		clearData();
		initLists();
		startTransaction();
	}

	private void clearData() throws Exception {
		utx.begin();
		em.joinTransaction();
		System.out.println("Dumping old records...");
		em.createQuery("DELETE FROM UserAcc").executeUpdate();
		utx.commit();
	}

	private void initLists() {
		expectedUsers = new ArrayList<UserAcc>();
		actualUsers = new ArrayList<UserAcc>();
		actualCustomers = new ArrayList<Customer>();
	}

	private void startTransaction() throws Exception {
		utx.begin();
		em.joinTransaction();
	}

	@After
	public void commitTransaction() throws Exception {
		utx.commit();
	}

	@Test
	@InSequence(1)
	public void add_userAcc() {

		// given
		UserAcc userAcc1 = new UserAcc("login1", "password1", "email1");
		expectedUsers.add(userAcc1);
		UserAcc userAcc2 = new UserAcc("login2", "password2", "email2");
		expectedUsers.add(userAcc2);
		UserAcc userAcc3 = new UserAcc("login3", "password3", "email3");
		expectedUsers.add(userAcc3);
		UserAcc userAcc4 = new UserAcc("login4", "password4", "email4");
		expectedUsers.add(userAcc4);

		// when
		for (UserAcc userAcc : expectedUsers) {
			em.persist(userAcc);
		}
		em.flush();
		em.clear();

		actualUsers = em.createQuery(SQL_USERACC, UserAcc.class).getResultList();
		for (UserAcc userAcc : actualUsers) {
			System.out.println(userAcc);
		}

		// then
		Assert.assertEquals(expectedUsers.size(), actualUsers.size());
		Assert.assertTrue(actualUsers.containsAll(expectedUsers));
	}

	@Test
	@InSequence(2)
	public void add_userAcc_with_customer() {

		// given
		UserAcc userAcc1 = new UserAcc("login1", "password1", "email1");
		Customer customer1 = new Customer("firstName1", "lastName1");
		userAcc1.setCustomer(customer1);
		customer1.setUser(userAcc1);
		expectedUsers.add(userAcc1);
		UserAcc userAcc2 = new UserAcc("login2", "password2", "email2");
		Customer customer2 = new Customer("firstName2", "lastName2");
		userAcc2.setCustomer(customer2);
		customer2.setUser(userAcc2);
		expectedUsers.add(userAcc2);
		UserAcc userAcc3 = new UserAcc("login3", "password3", "email3");
		Customer customer3 = new Customer("firstName3", "lastName3");
		userAcc3.setCustomer(customer3);
		customer3.setUser(userAcc3);
		expectedUsers.add(userAcc3);
		UserAcc userAcc4 = new UserAcc("login4", "password4", "email4");
		Customer customer4 = new Customer("firstName4", "lastName4");
		userAcc4.setCustomer(customer4);
		customer4.setUser(userAcc4);
		expectedUsers.add(userAcc4);

		// when
		for (UserAcc userAcc : expectedUsers) {
			em.persist(userAcc);
		}
		em.flush();
		em.clear();

		actualUsers = em.createQuery(SQL_USERACC, UserAcc.class).getResultList();
		actualCustomers = em.createQuery(SQL_CUSTOMER, Customer.class).getResultList();
		for (UserAcc userAcc : actualUsers) {
			System.out.println(userAcc);
		}

		// then
		Assert.assertEquals(expectedUsers.size(), actualUsers.size());
		Assert.assertTrue(actualUsers.containsAll(expectedUsers));
		Assert.assertTrue(actualCustomers.contains(expectedUsers.get(0).getCustomer()));
	}
}
