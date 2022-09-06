package com.woo.board.entity.items;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class ItemImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_img_id")
    private Long id;

    private String imgName; /// 이미지 이름
    private String oriImgName; // 이미지 원본 이름
    private String imgUrl; // 이미지 경로
    private String repimgYn; // 대표이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Items items;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
