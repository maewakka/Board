package com.woo.board.controller;

import com.woo.board.config.auth.LoginUser;
import com.woo.board.config.auth.SessionUser;
import com.woo.board.dto.orders.CartListDto;
import com.woo.board.dto.orders.OrderFormDto;
import com.woo.board.dto.orders.OrderListDto;
import com.woo.board.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Log
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/cart/add")
    public String addCart(@LoginUser SessionUser user, OrderFormDto orderFormDto) {
        log.info(orderFormDto.getItemId().toString());
        orderService.addCart(user, orderFormDto);
        return "redirect:/shop";
    }

    @GetMapping("/cart")
    public String cartDtl(@LoginUser SessionUser user, Model model) {
        List<CartListDto> cartList = orderService.getCartList(user);
        model.addAttribute("cartList", cartList);
        return "orders/cartList";
    }

    @PostMapping("/order/add")
    public String addOrder(@LoginUser SessionUser user, OrderFormDto orderFormDto, Model model) {
        try {
            orderService.addOrder(user, orderFormDto);
            model.addAttribute("message", "주문이 성공적으로 처리되었습니다.");
        } catch (Exception e) {
            model.addAttribute("message", "주문 중 에러가 발생되었습니다. 다시 시도해주십시오.");
        }
        return "redirect:/shop/items/" + orderFormDto.getItemId();
    }

    @GetMapping("/order")
    public String orderDtl(@LoginUser SessionUser user, Model model) {

        List<OrderListDto> orderListDtoList = orderService.getOrderList(user);
        model.addAttribute("orderListDto", orderListDtoList);

        return "orders/orderList";
    }

}
