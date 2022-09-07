package com.woo.board.entity.boards;

import com.woo.board.entity.BaseEntity;
import com.woo.board.entity.users.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Boards board;

    @Builder
    public Chat(String content, Boards board) {
        this.content = content;
        this.board = board;
    }

}
