package pl.chemik77;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import pl.chemik77.model.Address;
import pl.chemik77.model.Customer;
import pl.chemik77.model.Ordery;
import pl.chemik77.model.OrderItem;
import pl.chemik77.model.Phone;
import pl.chemik77.model.Product;
import pl.chemik77.model.User;
import pl.chemik77.utils.Calculator;

public class Main {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("manager1");
		EntityManager em = emf.createEntityManager();

		addRecords1(em);

		em.close();
		emf.close();
	}

	private static void addRecords1(EntityManager em) {

		User user = new User();
		Customer customer = new Customer();
		Address address = new Address();
		Phone phone1 = new Phone();
		Phone phone2 = new Phone();
		Ordery order = new Ordery();
		OrderItem orderItem = new OrderItem();
		OrderItem orderItem2 = new OrderItem();
		Product product = new Product();
		Product product2 = new Product();

		List<Phone> phones = new ArrayList<>();
		List<Address> addresses = new ArrayList<>();
		List<Ordery> orders = new ArrayList<>();
		List<OrderItem> orderItems = new ArrayList<>();

		product.setName("Jasmine Shower Gel 250ml");
		product.setPrice(10.00);
		orderItem.setProduct(product);

		product2.setName("Big Eye Mascara 8.7ml");
		product2.setPrice(22.00);
		orderItem2.setProduct(product2);

		orderItem.setQuantity(1);
		orderItem.setTotal(Calculator.calculateTotalItem(orderItem));
		orderItem.setOrdery(order);
		orderItems.add(orderItem);
		orderItem.setOrdery(order);

		orderItem2.setQuantity(1);
		orderItem2.setTotal(Calculator.calculateTotalItem(orderItem2));
		orderItem2.setOrdery(order);
		orderItems.add(orderItem2);
		orderItem2.setOrdery(order);

		order.setCode("17/01/0421");
		order.setTotalPrice(Calculator.calculateOrderPrice(orderItems));
		order.setOrderDate(LocalDate.now());
		order.setActive(true);
		order.setCustomer(customer);
		order.setOrderItems(orderItems);
		orders.add(order);

		address.setSteet("Jesionowa");
		address.setHouseNo("15/1");
		address.setZipCode("00-521");
		address.setCity("Warszawa");
		address.setCountry("Polska");
		addresses.add(address);

		phone1.setNumber("754 522 555");
		phones.add(phone1);
		phone2.setNumber("805 541 114");
		phones.add(phone2);

		customer.setFirstName("Jan");
		customer.setLastName("Kowalski");
		customer.setUser(user);
		customer.setPhones(phones);
		customer.setAddresses(addresses);
		customer.setOrders(orders);

		user.setLogin("user1");
		user.setPassword("password1");
		user.setEmail("user1@gmail.com");
		user.setCustomer(customer);

		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();

	}
}
