package com.woo.board.repository.items;

import com.woo.board.entity.items.ItemImg;
import com.woo.board.entity.items.Items;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItems(Items item);
}
