package com.woo.board.repository.items;

import com.woo.board.dto.items.ItemSearchDto;
import com.woo.board.dto.items.ItemViewDto;
import com.woo.board.dto.items.ItemsDto;
import com.woo.board.entity.items.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemsRepositoryCustom {

    Page<ItemsDto> getItemsPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<Items> getRegItemsPage(ItemSearchDto itemSearchDto, Pageable pageable, String userEmail);
}
