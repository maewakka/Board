package com.woo.board.dto.orders;

import lombok.Builder;
import lombok.Data;

@Data
public class OrderListDto {

    private String itemName;
    private int itemNum;
    private int itemPrice;
    private String itemImg;
    private int total;

    @Builder
    public OrderListDto(String itemName, int itemNum, int itemPrice, String itemImg, int total) {
        this.itemName = itemName;
        this.itemNum = itemNum;
        this.itemPrice = itemPrice;
        this.itemImg = itemImg;
        this.total = total;
    }
}
