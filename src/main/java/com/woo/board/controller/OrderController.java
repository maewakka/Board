package com.woo.board.controller;

import com.woo.board.config.auth.LoginUser;
import com.woo.board.config.auth.SessionUser;
import com.woo.board.dto.orders.CartListDto;
import com.woo.board.dto.orders.OrderFormDto;
import com.woo.board.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/cart/add")
    private String addCart(@LoginUser SessionUser user, OrderFormDto orderFormDto, Model model) {
        orderService.addCart(user, orderFormDto);
        return "redirect:/shop";
    }

    @GetMapping("/cart")
    private String cartDtl(@LoginUser SessionUser user, Model model) {
        List<CartListDto> cartList = orderService.getCartList(user);
        model.addAttribute("cartList", cartList);
        return "/orders/cartList";
    }

}
