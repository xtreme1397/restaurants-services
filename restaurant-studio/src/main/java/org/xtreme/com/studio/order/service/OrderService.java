package org.xtreme.com.studio.order.service;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.xtreme.com.auth.domain.UserSession;
import org.xtreme.com.studio.order.domain.Order;

public interface OrderService {
	Order createOrder(Order order,UserSession authUser);

	List<Order> getOrders(Pageable page);
}
