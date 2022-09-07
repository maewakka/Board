package com.woo.board.dto.boards;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatListDto {

    private String content;
    private String userName;
    private LocalDateTime createdDate;

    @Builder
    public ChatListDto(String content, String userName, LocalDateTime createdDate) {
        this.content = content;
        this.userName = userName;
        this.createdDate = createdDate;
    }
}
