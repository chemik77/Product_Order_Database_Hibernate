package pl.chemik77.model;

import java.time.LocalDate;
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

@RunWith(Arquillian.class)
public class CustomerTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Customer.class.getPackage())
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@PersistenceContext
	EntityManager em;

	@Inject
	UserTransaction utx;

	private final String SQL_CUSTOMER = "SELECT c FROM Customer c ORDER BY c.id";
	private final String SQL_ADDRESS = "SELECT a FROM Address a ORDER BY a.id";
	private final String SQL_CUSTOMERORDER = "SELECT c FROM CustomerOrder c ORDER BY c.id";

	private List<Customer> expectedCustomers;
	private List<Customer> actualCustomers;
	private List<Address> actualAddresses;
	private List<CustomerOrder> actualCustomersOrders;

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
		em.createQuery("DELETE FROM Customer").executeUpdate();
		utx.commit();
	}

	private void initLists() {
		expectedCustomers = new ArrayList<Customer>();
		actualCustomers = new ArrayList<Customer>();
		actualAddresses = new ArrayList<Address>();
		actualCustomersOrders = new ArrayList<CustomerOrder>();
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
	public void add_customer() {

		// given
		Customer customer1 = new Customer("firstName1", "lastName1");
		expectedCustomers.add(customer1);
		Customer customer2 = new Customer("firstName2", "lastName2");
		expectedCustomers.add(customer2);

		// when
		for (Customer customer : expectedCustomers) {
			em.persist(customer);
		}
		em.flush();
		em.clear();

		actualCustomers = em.createQuery(SQL_CUSTOMER, Customer.class).getResultList();
		for (Customer customer : actualCustomers) {
			System.out.println(customer);
		}

		// then
		Assert.assertEquals(expectedCustomers.size(), actualCustomers.size());
		Assert.assertTrue(actualCustomers.containsAll(expectedCustomers));
	}

	@Test
	@InSequence(2)
	public void add_customer_with_address() {

		// given
		Customer customer1 = new Customer("firstName1", "lastName1");
		List<Address> addresses1 = new ArrayList<Address>();
		Address address1 = new Address("street1", "1", "11-111", "city1", "country1");
		Address address2 = new Address("street2", "2", "22-222", "city2", "country2");
		addresses1.add(address1);
		addresses1.add(address2);
		customer1.setAddresses(addresses1);
		expectedCustomers.add(customer1);

		Customer customer2 = new Customer("firstName2", "lastName2");
		List<Address> addresses2 = new ArrayList<Address>();
		Address address3 = new Address("street3", "3", "33-333", "city3", "country3");
		Address address4 = new Address("street4", "4", "44-444", "city4", "country4");
		addresses2.add(address3);
		addresses2.add(address4);
		customer2.setAddresses(addresses2);
		expectedCustomers.add(customer2);

		// when
		for (Customer customer : expectedCustomers) {
			em.persist(customer);
		}
		em.flush();
		em.clear();

		actualCustomers = em.createQuery(SQL_CUSTOMER, Customer.class).getResultList();
		actualAddresses = em.createQuery(SQL_ADDRESS, Address.class).getResultList();
		for (Customer customer : actualCustomers) {
			System.out.println(customer);
		}

		// then
		Assert.assertEquals(expectedCustomers.size(), actualCustomers.size());
		Assert.assertTrue(actualCustomers.containsAll(expectedCustomers));
		Assert.assertTrue(actualAddresses.contains(expectedCustomers.get(0).getAddresses().get(0)));

	}

	@Test
	@InSequence(3)
	public void add_customer_with_customerOrder() {
		// given
		Customer customer1 = new Customer("firstName1", "lastName1");
		List<CustomerOrder> customerOrders1 = new ArrayList<CustomerOrder>();
		CustomerOrder customerOrder1 = new CustomerOrder("code1", LocalDate.now(), true, Status.WAITING_FOR_PAYMENT);
		CustomerOrder customerOrder2 = new CustomerOrder("code2", LocalDate.now(), true, Status.WAITING_FOR_PAYMENT);
		customerOrder1.setCustomer(customer1);
		customerOrder2.setCustomer(customer1);
		customerOrders1.add(customerOrder1);
		customerOrders1.add(customerOrder2);
		customer1.setCustomerOrders(customerOrders1);
		expectedCustomers.add(customer1);

		Customer customer2 = new Customer("firstName2", "lastName2");
		List<CustomerOrder> customerOrders2 = new ArrayList<CustomerOrder>();
		CustomerOrder customerOrder3 = new CustomerOrder("code3", LocalDate.now(), true, Status.WAITING_FOR_PAYMENT);
		CustomerOrder customerOrder4 = new CustomerOrder("code4", LocalDate.now(), true, Status.WAITING_FOR_PAYMENT);
		customerOrder3.setCustomer(customer2);
		customerOrder4.setCustomer(customer2);
		customerOrders2.add(customerOrder3);
		customerOrders2.add(customerOrder4);
		customer2.setCustomerOrders(customerOrders2);
		expectedCustomers.add(customer2);

		// when
		for (Customer customer : expectedCustomers) {
			em.persist(customer);
		}
		em.flush();
		em.clear();

		actualCustomers = em.createQuery(SQL_CUSTOMER, Customer.class).getResultList();
		actualCustomersOrders = em.createQuery(SQL_CUSTOMERORDER, CustomerOrder.class).getResultList();
		for (Customer customer : actualCustomers) {
			System.out.println(customer);
		}

		// then
		Assert.assertEquals(expectedCustomers.size(), actualCustomers.size());
		Assert.assertTrue(actualCustomers.containsAll(expectedCustomers));
		Assert.assertTrue(actualCustomersOrders.contains(expectedCustomers.get(0).getCustomerOrders().get(0)));
	}

}
