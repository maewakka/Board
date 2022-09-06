package com.woo.board.repository.items;

import com.woo.board.entity.items.Items;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemsRepository extends JpaRepository<Items, Long>, ItemsRepositoryCustom {

    List<Items> findByItemName(String itemName);
    List<Items> findByItemNameOrItemDetails(String itemName, String itemDetail);
    List<Items> findByItemPriceLessThan(Integer itemPrice);
    List<Items> findByItemPriceLessThanOrderByItemPriceDesc(Integer itemPrice);
}
