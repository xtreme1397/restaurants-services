package org.xtreme.com.studio.order.service;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.xtreme.com.auth.domain.LoggedInUser;
import org.xtreme.com.studio.order.domain.Order;

public interface OrderService {
	Order createOrder(Order order,LoggedInUser loggedInUser);

	List<Order> getOrders(Pageable page);
}
