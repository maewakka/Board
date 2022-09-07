package com.woo.board.repository.orders;

import com.woo.board.entity.orders.Orders;
import com.woo.board.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    List<Orders> findByUser(User user);
}
