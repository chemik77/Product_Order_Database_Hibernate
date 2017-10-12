package pl.chemik77.model;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CustomerOrder.class)
public abstract class CustomerOrder_ {

	public static volatile SingularAttribute<CustomerOrder, String> code;
	public static volatile SingularAttribute<CustomerOrder, Double> totalPrice;
	public static volatile SingularAttribute<CustomerOrder, Boolean> active;
	public static volatile SingularAttribute<CustomerOrder, Integer> id;
	public static volatile SingularAttribute<CustomerOrder, LocalDate> orderDate;
	public static volatile ListAttribute<CustomerOrder, OrderItem> orderItems;
	public static volatile SingularAttribute<CustomerOrder, Status> status;
	public static volatile SingularAttribute<CustomerOrder, Customer> customer;

}

