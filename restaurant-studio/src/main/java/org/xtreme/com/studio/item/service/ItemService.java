package org.xtreme.com.studio.item.service;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.xtreme.com.auth.domain.UserSession;
import org.xtreme.com.studio.item.domain.Item;

public interface ItemService {
	Item addItem(Item item,UserSession authUser);

	List<Item> getItems(Pageable pageable);

	Item getItemById(String id);
}
