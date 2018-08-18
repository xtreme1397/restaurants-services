package org.xtreme.com.studio.item.controller;

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
import org.xtreme.com.studio.item.domain.Item;
import org.xtreme.com.studio.item.service.ItemService;

@RestController
@RequestMapping(value = "/v1")
public class ItemController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
	@Autowired
	ItemService itemService;

	@RequestMapping(value = "/items", method = RequestMethod.POST)
	public Item addItem(@RequestBody Item item, Authentication authentication) {
		LoggedInUser loggedInUser = (LoggedInUser) authentication.getPrincipal();
		LOGGER.info("add Item request from {}", loggedInUser.getName());
		return itemService.addItem(item, loggedInUser);
	}

	@RequestMapping(value = "/items", method = RequestMethod.GET)
	public List<Item> getItems(Pageable pageable, Authentication authentication) {
		LoggedInUser loggedInUser = (LoggedInUser) authentication.getPrincipal();
		LOGGER.info("get Items request from {}", loggedInUser.getName());
		return itemService.getItems(pageable);
	}
}
