package pl.chemik77.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class CustomerOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String code;
	@Column(scale = 2, name = "total_price")
	private double totalPrice;
	@Column(name = "order_date")
	private LocalDate orderDate;
	private boolean active;
	private Status status;

	@ManyToOne
	private Customer customer;

	@OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems;

	public CustomerOrder() {

	}

	public CustomerOrder(String code, LocalDate orderDate, boolean active, Status status) {
		this.code = code;
		this.orderDate = orderDate;
		this.active = active;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@Override
	public String toString() {
		return "CustomerOrder [id=" + id + ", code=" + code + ", totalPrice=" + totalPrice + ", customer=" + customer
				+ "]";
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((orderDate == null) ? 0 : orderDate.hashCode());
		long temp;
		temp = Double.doubleToLongBits(totalPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CustomerOrder)) {
			return false;
		}
		CustomerOrder other = (CustomerOrder) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		if (orderDate == null) {
			if (other.orderDate != null) {
				return false;
			}
		} else if (!orderDate.equals(other.orderDate)) {
			return false;
		}
		if (Double.doubleToLongBits(totalPrice) != Double.doubleToLongBits(other.totalPrice)) {
			return false;
		}
		return true;
	}

}
