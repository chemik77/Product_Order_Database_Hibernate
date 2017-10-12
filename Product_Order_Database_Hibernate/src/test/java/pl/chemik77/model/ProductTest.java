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
public class ProductTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class).addPackage(Product.class.getPackage())
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@PersistenceContext
	EntityManager em;

	@Inject
	UserTransaction utx;

	private final String SQL_PRODUCT = "SELECT p FROM Product p ORDER BY p.id";

	private List<Product> expectedProducts;
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
		em.createQuery("DELETE FROM Product").executeUpdate();
		utx.commit();
	}

	private void initLists() {
		expectedProducts = new ArrayList<Product>();
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
	public void add_product() {

		// given
		Product product1 = new Product("name1", 10);
		expectedProducts.add(product1);
		Product product2 = new Product("name2", 20);
		expectedProducts.add(product2);

		// when
		for (Product product : expectedProducts) {
			em.persist(product);
		}
		em.flush();
		em.clear();

		actualProducts = em.createQuery(SQL_PRODUCT, Product.class).getResultList();
		for (Product product : actualProducts) {
			System.out.println(product);
		}

		// then
		Assert.assertEquals(expectedProducts.size(), actualProducts.size());
		Assert.assertTrue(actualProducts.containsAll(expectedProducts));
	}

}
