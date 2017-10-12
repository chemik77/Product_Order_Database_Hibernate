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
public class CustomerOrderTest {

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

	private final String SQL_CUSTOMERORDER = "SELECT c FROM CustomerOrder c ORDER BY c.id";
	private final String SQL_ORDERITEM = "SELECT o FROM OrderItem o ORDER BY o.id";

	private List<CustomerOrder> expectedCustomerOrders;
	private List<CustomerOrder> actualCustomerOrders;
	private List<OrderItem> actualOrderItems;

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
		em.createQuery("DELETE FROM CustomerOrder").executeUpdate();
		utx.commit();
	}

	private void initLists() {
		expectedCustomerOrders = new ArrayList<CustomerOrder>();
		actualCustomerOrders = new ArrayList<CustomerOrder>();
		actualOrderItems = new ArrayList<OrderItem>();
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
	public void add_customerOrder() {

		// given
		CustomerOrder customerOrder1 = new CustomerOrder("code1", LocalDate.now(), true, Status.WAITING_FOR_PAYMENT);
		expectedCustomerOrders.add(customerOrder1);
		CustomerOrder customerOrder2 = new CustomerOrder("code2", LocalDate.now(), true, Status.WAITING_FOR_PAYMENT);
		expectedCustomerOrders.add(customerOrder2);

		// when
		for (CustomerOrder customerOrder : expectedCustomerOrders) {
			em.persist(customerOrder);
		}
		em.flush();
		em.clear();

		actualCustomerOrders = em.createQuery(SQL_CUSTOMERORDER, CustomerOrder.class).getResultList();
		for (CustomerOrder customerOrder : actualCustomerOrders) {
			System.out.println(customerOrder);
		}

		// then
		Assert.assertEquals(expectedCustomerOrders.size(), actualCustomerOrders.size());
		Assert.assertTrue(actualCustomerOrders.containsAll(actualCustomerOrders));

	}

	@Test
	@InSequence(2)
	public void add_customerOrder_with_orderItem() {

		// given
		CustomerOrder customerOrder1 = new CustomerOrder("code1", LocalDate.now(), true, Status.WAITING_FOR_PAYMENT);
		List<OrderItem> orderItems1 = new ArrayList<OrderItem>();
		OrderItem orderItem1 = new OrderItem(1);
		orderItem1.setCustomerOrder(customerOrder1);
		orderItems1.add(orderItem1);
		OrderItem orderItem2 = new OrderItem(2);
		orderItem2.setCustomerOrder(customerOrder1);
		orderItems1.add(orderItem2);
		customerOrder1.setOrderItems(orderItems1);
		expectedCustomerOrders.add(customerOrder1);

		CustomerOrder customerOrder2 = new CustomerOrder("code2", LocalDate.now(), true, Status.WAITING_FOR_PAYMENT);
		List<OrderItem> orderItems2 = new ArrayList<OrderItem>();
		OrderItem orderItem3 = new OrderItem(3);
		orderItem3.setCustomerOrder(customerOrder2);
		orderItems2.add(orderItem3);
		OrderItem orderItem4 = new OrderItem(4);
		orderItem4.setCustomerOrder(customerOrder2);
		orderItems2.add(orderItem4);
		customerOrder2.setOrderItems(orderItems2);
		expectedCustomerOrders.add(customerOrder2);

		// when
		for (CustomerOrder customerOrder : expectedCustomerOrders) {
			em.persist(customerOrder);
		}
		em.flush();
		em.clear();

		actualCustomerOrders = em.createQuery(SQL_CUSTOMERORDER, CustomerOrder.class).getResultList();
		actualOrderItems = em.createQuery(SQL_ORDERITEM, OrderItem.class).getResultList();
		for (CustomerOrder customerOrder : actualCustomerOrders) {
			System.out.println(customerOrder);
		}

		// then
		Assert.assertEquals(expectedCustomerOrders.size(), actualCustomerOrders.size());
		Assert.assertTrue(actualCustomerOrders.containsAll(actualCustomerOrders));
		Assert.assertTrue(actualOrderItems.contains(expectedCustomerOrders.get(0).getOrderItems().get(0)));
	}
}
