package pl.chemik77.model;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
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
public class AddressTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class).addPackage(Address.class.getPackage())
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@PersistenceContext
	EntityManager em;

	@Inject
	UserTransaction utx;

	private final String SQL_ADDRESS = "SELECT a FROM Address a ORDER BY a.id";

	private List<Address> expectedAddresses;
	private List<Address> actualAddresses;

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
		em.createQuery("DELETE FROM Address").executeUpdate();
		utx.commit();
	}

	private void initLists() {
		expectedAddresses = new ArrayList<Address>();
		actualAddresses = new ArrayList<Address>();
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
	public void add_address() {

		// given
		Address address1 = new Address("street1", "1", "11-111", "city1", "country1");
		expectedAddresses.add(address1);
		Address address2 = new Address("street2", "2", "22-222", "city2", "country2");
		expectedAddresses.add(address2);

		// when
		for (Address address : expectedAddresses) {
			em.persist(address);
		}
		em.flush();
		em.clear();

		actualAddresses = em.createQuery(SQL_ADDRESS, Address.class).getResultList();
		for (Address address : actualAddresses) {
			System.out.println(address);
		}

		// then
		Assert.assertEquals(expectedAddresses.size(), actualAddresses.size());
		Assert.assertTrue(actualAddresses.containsAll(expectedAddresses));

	}
}
