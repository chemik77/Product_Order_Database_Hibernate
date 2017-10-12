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

@RunWith(Arquillian.class)
public class OrderItemTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class).addPackage(OrderItem.class.getPackage())
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@PersistenceContext
	EntityManager em;

	@Inject
	UserTransaction utx;

	private final String SQL_ORDERITEM = "SELECT o FROM OrderItem o ORDER BY o.id";
	private final String SQL_PRODUCT = "SELECT p FROM Product p ORDER BY p.id";

	private List<OrderItem> expectedOrderItems;
	private List<OrderItem> actualOrderItems;
	private List<Product> actualProducts;

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
		em.createQuery("DELETE FROM OrderItem").executeUpdate();
		utx.commit();
	}

	private void initLists() {
		expectedOrderItems = new ArrayList<OrderItem>();
		actualOrderItems = new ArrayList<OrderItem>();
		actualProducts = new ArrayList<Product>();
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
	public void add_orderItem() {

		// given
		OrderItem orderItem1 = new OrderItem(1);
		expectedOrderItems.add(orderItem1);

		OrderItem orderItem2 = new OrderItem(2);
		expectedOrderItems.add(orderItem2);

		// when
		for (OrderItem orderItem : expectedOrderItems) {
			em.persist(orderItem);
		}
		em.flush();
		em.clear();

		actualOrderItems = em.createQuery(SQL_ORDERITEM, OrderItem.class).getResultList();
		for (OrderItem orderItem : actualOrderItems) {
			System.out.println(orderItem);
		}

		// then
		Assert.assertEquals(expectedOrderItems.size(), actualOrderItems.size());
		Assert.assertTrue(actualOrderItems.containsAll(expectedOrderItems));
	}

	@Test
	@InSequence(2)
	public void add_orderItem_with_product() {

		// given
		OrderItem orderItem1 = new OrderItem(1);
		Product product1 = new Product("name1", 10);
		orderItem1.setProduct(product1);
		expectedOrderItems.add(orderItem1);

		OrderItem orderItem2 = new OrderItem(2);
		Product product2 = new Product("name2", 20);
		orderItem2.setProduct(product2);
		expectedOrderItems.add(orderItem2);

		// when
		for (OrderItem orderItem : expectedOrderItems) {
			em.persist(orderItem);
		}
		em.flush();
		em.clear();

		actualOrderItems = em.createQuery(SQL_ORDERITEM, OrderItem.class).getResultList();
		actualProducts = em.createQuery(SQL_PRODUCT, Product.class).getResultList();
		for (OrderItem orderItem : actualOrderItems) {
			System.out.println(orderItem);
		}

		// then
		Assert.assertEquals(expectedOrderItems.size(), actualOrderItems.size());
		Assert.assertTrue(actualOrderItems.containsAll(expectedOrderItems));
		Assert.assertTrue(actualProducts.contains(expectedOrderItems.get(0).getProduct()));
	}
}
