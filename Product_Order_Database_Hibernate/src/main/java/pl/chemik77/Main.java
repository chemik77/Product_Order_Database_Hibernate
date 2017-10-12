package pl.chemik77;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import pl.chemik77.model.*;
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

		UserAcc userAcc = new UserAcc();
		Customer customer = new Customer();
		Address address = new Address();
		CustomerOrder customerOrder = new CustomerOrder();
		OrderItem orderItem = new OrderItem();
		OrderItem orderItem2 = new OrderItem();
		Product product = new Product();
		Product product2 = new Product();

		List<String> phones = new ArrayList<>();
		List<Address> addresses = new ArrayList<>();
		List<CustomerOrder> customerOrders = new ArrayList<>();
		List<OrderItem> orderItems = new ArrayList<>();

		product.setName("Jasmine Shower Gel 250ml");
		product.setPrice(10.00);
		orderItem.setProduct(product);

		product2.setName("Big Eye Mascara 8.7ml");
		product2.setPrice(22.00);
		orderItem2.setProduct(product2);

		orderItem.setQuantity(1);
		orderItem.setTotal(Calculator.calculateTotalItem(orderItem));
		orderItem.setCustomerOrder(customerOrder);
		orderItems.add(orderItem);
		orderItem.setCustomerOrder(customerOrder);

		orderItem2.setQuantity(1);
		orderItem2.setTotal(Calculator.calculateTotalItem(orderItem2));
		orderItem2.setCustomerOrder(customerOrder);
		orderItems.add(orderItem2);
		orderItem2.setCustomerOrder(customerOrder);

		customerOrder.setCode("17/01/0421");
		customerOrder.setTotalPrice(Calculator.calculateOrderPrice(orderItems));
		customerOrder.setActive(true);
		customerOrder.setCustomer(customer);
		customerOrder.setOrderItems(orderItems);
		customerOrders.add(customerOrder);

		address.setSteet("Jesionowa");
		address.setHouseNo("15/1");
		address.setZipCode("00-521");
		address.setCity("Warszawa");
		address.setCountry("Polska");
		addresses.add(address);

		phones.add("754 522 555");
		phones.add("805 541 114");

		customer.setFirstName("Jan");
		customer.setLastName("Kowalski");
		customer.setUser(userAcc);
		customer.setPhones(phones);
		customer.setAddresses(addresses);
		customer.setCustomerOrders(customerOrders);

		userAcc.setLogin("user1");
		userAcc.setPassword("password1");
		userAcc.setEmail("user1@gmail.com");
		userAcc.setCustomer(customer);

		em.getTransaction().begin();
		em.persist(userAcc);
		em.getTransaction().commit();

	}
}
