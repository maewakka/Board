package com.woo.board.dto.orders;

import com.woo.board.config.auth.SessionUser;
import lombok.Data;

@Data
public class OrderFormDto {
    private Long itemId;
    private int itemNum;
}
