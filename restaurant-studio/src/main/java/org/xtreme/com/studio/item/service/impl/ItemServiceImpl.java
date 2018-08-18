package org.xtreme.com.studio.item.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.xtreme.com.auth.domain.LoggedInUser;
import org.xtreme.com.auth.domain.UserSession;
import org.xtreme.com.studio.item.data.ItemRepository;
import org.xtreme.com.studio.item.domain.Item;
import org.xtreme.com.studio.item.service.ItemService;
@Service()
public class ItemServiceImpl implements ItemService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemServiceImpl.class);
	@Autowired
	ItemRepository itemRepository;

	@Override
	public Item addItem(Item item, UserSession authUser) {
		LoggedInUser loggedInUser = (LoggedInUser) authUser.getPrincipal();
		LOGGER.info("new item creation requset came from : {}", loggedInUser.getName());
		item.setCreatedBy(loggedInUser.getName());
		item.setCreatedAt(new Date());
		return itemRepository.save(item);

	}

	@Override
	public List<Item> getItems(Pageable pageable) {
		return itemRepository.findAll(pageable).getContent();
	}

	@Override
	public Item getItemById(String id) {
		return itemRepository.findOne(id);
	}

}
