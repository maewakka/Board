package com.woo.board.entity.boards;

import com.woo.board.entity.items.Items;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class BoardImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName; /// 이미지 이름
    private String oriImgName; // 이미지 원본 이름
    private String imgUrl; // 이미지 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Boards board;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
