package com.woo.board.dto.orders;

import lombok.Builder;
import lombok.Data;

@Data
public class CartListDto {

    private String itemName;
    private int itemNum;
    private int itemPrice;

    @Builder
    public CartListDto(String itemName, int itemNum, int itemPrice) {
        this.itemName = itemName;
        this.itemNum = itemNum;
        this.itemPrice = itemPrice;
    }
}
