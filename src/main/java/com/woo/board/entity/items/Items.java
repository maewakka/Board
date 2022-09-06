package com.woo.board.entity.items;

import com.woo.board.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Items extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private int itemPrice;

    @Column(nullable = false)
    private int stockNumber;

    @Column(nullable = false)
    private String itemDetails;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;




}
