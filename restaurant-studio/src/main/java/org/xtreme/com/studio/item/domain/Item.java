package org.xtreme.com.studio.item.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data()
public class Item {
	@Id
	private String id;
	private String name;
	private String quantity;
	private String price;
	private String description;
	private String createdBy;
	private Date createdAt;
}
