package com.woo.board.entity.orders;

import com.woo.board.entity.BaseEntity;
import com.woo.board.entity.items.Items;
import com.woo.board.entity.users.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int itemNum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Items item;

    @Builder
    public Orders(int itemNum, User user, Items item) {
        this.itemNum = itemNum;
        this.user = user;
        this.item = item;
    }
}
