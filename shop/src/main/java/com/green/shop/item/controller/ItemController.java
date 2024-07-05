package com.green.shop.item.controller;

import com.green.shop.config.PageHandler;
import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.dto.ItemSearchDto;
import com.green.shop.item.form.ItemForm;
import com.green.shop.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor

public class ItemController {

    private final ItemService itemService;
    private final PageHandler pageHandler;

    @GetMapping("/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemForm", new ItemForm());
        return "item/itemForm";
    }

    @PostMapping("/admin/item/new")
    public String itemNew(@Valid ItemForm itemForm,
                          BindingResult bindingResult,
                          Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemForm.getId() == null) {
            model.addAttribute("errorMessage",
                                "첫번째 상품 이미지는 필수입니다.");
            return "item/itemForm";
        }

        try{

            itemService.itemInsert(itemForm, itemImgFileList);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage",
                                "상품 등록 중에 에러 발생");

        }

        return "redirect:/";
    }

    @GetMapping("/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId,
                          Model model) {

        try {
            ItemForm itemForm = itemService.getItemDtl(itemId);

            model.addAttribute("itemForm", itemForm);

        } catch (NullPointerException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemForm", new ItemForm());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping("/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemForm itemForm,
                             BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             Model model) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemForm.getId() == null) {
            model.addAttribute("errorMessage",
                                "첫번째 상품 이미지는 필수입니다.");
            return "item/itemForm";
        }

        try {
            System.out.println(itemForm);
            itemService.updateItem(itemForm, itemImgFileList);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage",
                                "상품 수정 중 에러 발생");
            return "item/itemForm";
        }
        return "redirect:/";

    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemListPage(@PathVariable(value="page", required = false) Integer page,
                               @ModelAttribute("itemSearchDto") ItemSearchDto itemSearchDto,
                               Model model) {
        int ps = 5; //한 화면에 표시될 데이터의 개수
        if (page == null) page = 1;

        Map map = new HashMap();

        //페이지 번호 : 1, sql의 offset : 0
        //페이지번호 : 2, sql의 offset : 10
        map.put("page", page * ps - ps);
        map.put("pageSize", ps);
        map.put("itemSearchDto", itemSearchDto);
        int totalCnt = itemService.countAdminItems(map);

        PageHandler pageHandler = new PageHandler(totalCnt, ps, page);

        List<ItemDto> items = itemService.itemListPage(map);

        model.addAttribute("pageHandler", pageHandler);
        model.addAttribute("items", items);

        return "item/itemMng";
    }

    @GetMapping("/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        ItemForm itemForm = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemForm);
        return "item/itemDtl";

    }
}
