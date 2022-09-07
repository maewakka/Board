package com.woo.board.dto.boards;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ChatFormDto {

    private Long boardId;
    private String content;
}
