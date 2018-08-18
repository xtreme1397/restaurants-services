package org.xtreme.com.studio.item.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.xtreme.com.studio.item.domain.Item;

public interface ItemRepository extends MongoRepository<Item, String> {

}
