package pl.chemik77.utils;

import java.util.List;

import pl.chemik77.model.OrderItem;

public class Calculator {

	private static double total = 0;
	
	public static double calculateOrderPrice(List<OrderItem> orderItems) {
		total = 0;
		orderItems.forEach(e -> {
			total += e.getTotal();
		});
		return total;
	}

	public static double calculateTotalItem(OrderItem orderItem) {
		total = 0;
		total = orderItem.getQuantity() * orderItem.getProduct().getPrice();
		return total;
	}
	
}
