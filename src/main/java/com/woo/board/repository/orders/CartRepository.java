package com.woo.board.repository.orders;

import com.woo.board.entity.items.Items;
import com.woo.board.entity.orders.Cart;
import com.woo.board.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser(User user);
    Cart findByItems(Items item);
}
