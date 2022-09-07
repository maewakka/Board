package com.woo.board.service;

import com.woo.board.config.auth.SessionUser;
import com.woo.board.dto.orders.CartListDto;
import com.woo.board.dto.orders.OrderFormDto;
import com.woo.board.dto.orders.OrderListDto;
import com.woo.board.entity.items.ItemImg;
import com.woo.board.entity.items.Items;
import com.woo.board.entity.orders.Cart;
import com.woo.board.entity.orders.Orders;
import com.woo.board.entity.users.User;
import com.woo.board.repository.items.ItemImgRepository;
import com.woo.board.repository.items.ItemsRepository;
import com.woo.board.repository.orders.CartRepository;
import com.woo.board.repository.orders.OrdersRepository;
import com.woo.board.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final ItemsRepository itemsRepository;
    private final UserRepository userRepository;
    private final OrdersRepository ordersRepository;
    private final ItemImgRepository itemImgRepository;

    @Transactional
    public Long addCart(SessionUser sessionUser, OrderFormDto orderFormDto) {
        Long itemId = orderFormDto.getItemId();
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(EntityNotFoundException::new);
        Items item = itemsRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        Cart cart = cartRepository.findByItems(item);
        Cart saveCart = new Cart();

        if(cart == null) {
            saveCart = cartRepository.save(Cart.builder()
                    .items(item)
                    .itemNum(orderFormDto.getItemNum())
                    .user(user)
                    .build());
        }
        else {
            cart.setItemNum(cart.getItemNum() + orderFormDto.getItemNum());
        }

        return saveCart.getId();
    }

    @Transactional
    public Long addOrder(SessionUser sessionUser, OrderFormDto orderFormDto) {
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(EntityNotFoundException::new);
        Items item = itemsRepository.findById(orderFormDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        return ordersRepository.save(Orders.builder()
                .itemNum(orderFormDto.getItemNum())
                .item(item)
                .user(user)
                .build()).getId();
    }

    @Transactional(readOnly = true)
    public List<CartListDto> getCartList(SessionUser sessionUser) {
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(EntityNotFoundException::new);
        List<Cart> cartList = cartRepository.findByUser(user);
        List<CartListDto> cartListDtoList = new ArrayList<>();

        for(int i=0; i<cartList.size(); i++) {
            Cart cart = cartList.get(i);
            Items item = itemsRepository.findById(cart.getItems().getId()).get();

            String itemName = item.getItemName();
            int itemPrice = item.getItemPrice();

            cartListDtoList.add(CartListDto.builder()
                            .itemName(itemName)
                            .itemPrice(itemPrice)
                            .itemNum(cart.getItemNum())
                            .build());
        }

        return cartListDtoList;
    }

    @Transactional(readOnly = true)
    public List<OrderListDto> getOrderList(SessionUser sessionUser) {
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(EntityNotFoundException::new);
        List<Orders> ordersList = ordersRepository.findByUser(user);
        List<OrderListDto> orderListDtoList = new ArrayList<>();

        for(int i=0; i<ordersList.size(); i++) {
            Orders order = ordersList.get(i);
            Items item = order.getItem();
            ItemImg itemImg = itemImgRepository.findByItems(item).get(0);

            orderListDtoList.add(OrderListDto.builder()
                            .itemName(item.getItemName())
                            .itemNum(order.getItemNum())
                            .itemPrice(item.getItemPrice())
                            .itemImg(itemImg.getImgUrl())
                            .total(item.getItemPrice() * order.getItemNum())
                            .build());
        }

        return orderListDtoList;
    }
}
