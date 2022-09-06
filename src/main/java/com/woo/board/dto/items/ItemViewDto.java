package com.woo.board.dto.items;

import com.querydsl.core.annotations.QueryProjection;
import com.woo.board.entity.items.Items;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ItemViewDto {

    private Long id;
    private String itemName;
    private String itemDetails;
    private List<String> imgUrlList;
    private Integer itemPrice;
    private Integer stockNumber;

    public ItemViewDto(Items item, List<String> imgUrlList) {
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.itemDetails = item.getItemDetails();
        this.imgUrlList = imgUrlList;
        this.itemPrice = item.getItemPrice();
        this.stockNumber = item.getStockNumber();
    }
}
