package pl.chemik77.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserAcc.class)
public abstract class UserAcc_ {

	public static volatile SingularAttribute<UserAcc, String> password;
	public static volatile SingularAttribute<UserAcc, Integer> id;
	public static volatile SingularAttribute<UserAcc, String> login;
	public static volatile SingularAttribute<UserAcc, String> email;
	public static volatile SingularAttribute<UserAcc, Customer> customer;

}

