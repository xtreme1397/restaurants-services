package org.xtreme.com.studio.order.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.xtreme.com.auth.domain.LoggedInUser;
import org.xtreme.com.studio.order.consts.OrderStatus;
import org.xtreme.com.studio.order.data.OrderRepository;
import org.xtreme.com.studio.order.domain.Order;
import org.xtreme.com.studio.order.service.OrderService;

@Service()
public class OrderServiceImpl implements OrderService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
	@Autowired
	OrderRepository orderRepository;

	@Override
	public Order createOrder(Order order, LoggedInUser loggedInUser) {
		LOGGER.info("new item creation requset came from : {}", loggedInUser.getName());
		order.setStatus(OrderStatus.INPROGRESS.getValue());
		order.setCreatedBy(loggedInUser.getName());
		order.setCreatedAt(new Date());
		return orderRepository.save(order);
	}

	@Override
	public List<Order> getOrders(Pageable pageable) {
		return orderRepository.findAll(pageable).getContent();
	}

}
