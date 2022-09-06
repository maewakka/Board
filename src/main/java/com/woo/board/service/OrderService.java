package com.woo.board.service;

import com.woo.board.config.auth.SessionUser;
import com.woo.board.dto.orders.CartListDto;
import com.woo.board.dto.orders.OrderFormDto;
import com.woo.board.entity.items.Items;
import com.woo.board.entity.orders.Cart;
import com.woo.board.entity.users.User;
import com.woo.board.repository.items.ItemsRepository;
import com.woo.board.repository.orders.CartRepository;
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
}
