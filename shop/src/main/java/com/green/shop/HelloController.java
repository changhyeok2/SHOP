package com.green.shop;

import com.green.shop.config.PageHandler;
import com.green.shop.item.dto.ItemMainDto;
import com.green.shop.item.dto.ItemSearchDto;
import com.green.shop.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HelloController {

    private final ItemService itemService;

    @GetMapping("/")
    public String main(
            @RequestParam(value="page", required = false) Integer page,
            @RequestParam(value="searchText", required = false) String searchText,
            @ModelAttribute("itemSearchDto") ItemSearchDto itemSearchDto,
            Model model
            ) {

        //페이지 크기
        int pg = 6;

        if (page == null) page = 1;

        Map map = new HashMap();

        System.out.println("page : " + page);
        System.out.println("searchText : " + searchText);
        System.out.println("itemSearchDto : " + itemSearchDto);

        if (searchText != null) {
            itemSearchDto.setSearchText(searchText);
            itemSearchDto.setSearchBy("itemName");
        }

        System.out.println("itemSearchDto : " + itemSearchDto);

        map.put("page", page * pg - pg);
        map.put("pageSize", pg);
        map.put("itemSearchDto", itemSearchDto);

        System.out.println("map : " + map);

        //개수 계산하기
        int totalCnt = itemService.countAdminItems(map);

        PageHandler pageHandler = new PageHandler(totalCnt, pg, page);

        List<ItemMainDto> items = itemService.mainSelect(map);

        model.addAttribute("items", items);
        model.addAttribute("pageHandler", pageHandler);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "index";
    }
}
