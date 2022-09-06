package com.woo.board.controller;

import com.woo.board.config.auth.LoginUser;
import com.woo.board.config.auth.SessionUser;
import com.woo.board.dto.UserInfoDto;
import com.woo.board.dto.items.ItemSearchDto;
import com.woo.board.dto.items.ItemsDto;
import com.woo.board.entity.boards.Boards;
import com.woo.board.entity.items.Items;
import com.woo.board.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final ItemService itemService;

    @GetMapping(value = {"/", "/shop", "/shop/{page}"})
    public String index(HttpServletRequest request, ItemSearchDto itemSearchDto, Model model,
                        @LoginUser SessionUser user, @PathVariable("page") Optional<Integer> page) {
        if(user != null) {
            UserInfoDto userInfoDto = new UserInfoDto();
            userInfoDto.updateUserInfo(user);
            model.addAttribute("userinfo", userInfoDto);
        }
        HttpSession session = request.getSession();
        if (session != null) {
            String redirectUrl = (String) session.getAttribute("prevPage");
            System.out.println(redirectUrl);
        }

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<ItemsDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }

}
