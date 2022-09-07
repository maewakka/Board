package com.woo.board.controller;

import com.woo.board.dto.items.ItemRegFormDto;
import com.woo.board.dto.items.ItemSearchDto;
import com.woo.board.dto.items.ItemViewDto;
import com.woo.board.dto.orders.OrderFormDto;
import com.woo.board.entity.items.Items;
import com.woo.board.service.ItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@Log
public class ItemController {

    private final ItemService itemService;
    private final HttpSession httpSession;

    @GetMapping("/items/register")
    public String registerDtl(Model model) {
        model.addAttribute("itemRegFormDto", new ItemRegFormDto());

        return "items/register";
    }

    @PostMapping("/items/register")
    public String item_register(@Valid ItemRegFormDto itemRegFormDto, BindingResult bindingResult,
                                Model model, @RequestPart("itemImgFile") List<MultipartFile> itemImgFileList) {
        if(bindingResult.hasErrors()) {
            return "items/register";
        }
        if(itemImgFileList.get(0).isEmpty()) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "items/register";
        }

        try {
            itemService.newRegister(itemRegFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "items/register";
        }

        return "redirect:/";
     }

    @GetMapping(value = {"/items/register/list", "/items/register/list/{page}"})
    public String registerDtl(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);

        Page<Items> items = itemService.getRegItemsListPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "items/registerList";
    }

    @GetMapping("/shop/items/{itemId}")
    public String itemDtl(@PathVariable("itemId")Long itemId, OrderFormDto orderFormDto, Model model) {
        ItemViewDto itemViewDto = itemService.getItem(itemId);
        model.addAttribute("itemViewDto", itemViewDto);
        model.addAttribute("orderFormDto", orderFormDto);

        return "items/itemView";
    }
}
