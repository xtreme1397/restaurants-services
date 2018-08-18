package org.xtreme.com.studio.order.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xtreme.com.auth.domain.LoggedInUser;
import org.xtreme.com.auth.domain.UserSession;
import org.xtreme.com.studio.order.domain.Order;
import org.xtreme.com.studio.order.service.OrderService;

@RestController
@RequestMapping(value = "/v1")
public class OrderController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	OrderService orderService;

	@RequestMapping(value = "/orders", method = RequestMethod.POST)
	public Order createOrder(@RequestBody Order order, Authentication authentication) {
		LoggedInUser loggedInUser = (LoggedInUser) authentication.getPrincipal();
		LOGGER.info("create order request from {}", loggedInUser.getName());
		return orderService.createOrder(order, loggedInUser);
	}

	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	public List<Order> getorders(Pageable pageable, Authentication authentication) {
		LoggedInUser loggedInUser = (LoggedInUser) authentication.getPrincipal();
		LOGGER.info("get orders request from {}", loggedInUser.getName());
		return orderService.getOrders(pageable);
	}
}
