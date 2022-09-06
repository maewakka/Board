package com.woo.board.dto.items;

import com.querydsl.core.annotations.QueryProjection;
import com.woo.board.entity.items.ItemSellStatus;
import lombok.Data;

@Data
public class ItemsDto {

    private Long id;
    private String itemName;
    private String itemDetails;
    private String imgUrl;
    private Integer itemPrice;

    @QueryProjection
    public ItemsDto(Long id, String itemName, String itemDetails, String imgUrl, Integer itemPrice) {
        this.id = id;
        this.itemName = itemName;
        this.itemDetails = itemDetails;
        this.imgUrl = imgUrl;
        this.itemPrice = itemPrice;
    }

}
