package org.xtreme.com.studio.order.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.xtreme.com.studio.item.domain.Item;

import lombok.Data;

@Data()
public class Order {
	@Id
	private String orderId;
	private String customerName;
	private String price;
	private String details;
	private List<Item> items;
	private Date createdAt;
	private String status;
	private String createdBy;
	private String mobile;
}
