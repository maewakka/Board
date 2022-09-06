package com.woo.board.entity.orders;

import com.woo.board.entity.items.Items;
import com.woo.board.entity.users.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Items items;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int itemNum;

    @Builder
    public Cart(Items items, int itemNum, User user) {
        this.items = items;
        this.itemNum = itemNum;
        this.user = user;
    }
}
