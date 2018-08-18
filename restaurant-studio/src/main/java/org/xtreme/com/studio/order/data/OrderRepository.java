package org.xtreme.com.studio.order.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.xtreme.com.studio.order.domain.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
	Order findByOrderId(String orderId);
}
