package com.woo.board.entity.boards;

import com.woo.board.dto.boards.BoardFormDto;
import com.woo.board.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Boards extends BaseEntity {

    @Id
    @Column(name = "boards_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, name = "boards_title")
    private String title;

    @Column(nullable = false, name = "boards_content")
    private String content;

    @Builder
    public Boards(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateBoard(BoardFormDto boardFormDto) {
        this.title = boardFormDto.getTitle();
        this.content = boardFormDto.getContent();
    }
}
