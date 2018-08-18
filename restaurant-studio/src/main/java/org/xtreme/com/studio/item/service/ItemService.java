package org.xtreme.com.studio.item.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.xtreme.com.auth.domain.LoggedInUser;
import org.xtreme.com.studio.item.domain.Item;

public interface ItemService {
	Item addItem(Item item, LoggedInUser loggedInUser);

	List<Item> getItems(Pageable pageable);

	Item getItemById(String id);
}
