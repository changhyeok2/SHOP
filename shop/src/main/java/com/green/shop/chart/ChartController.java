package com.green.shop.chart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChartController {

    private final ChartService chartService;

    @GetMapping("/chart")
    public String show(Model model){
        List<ChartDto> orderCounts =  chartService.orderCount();
        model.addAttribute("orderCounts", orderCounts);
        return "chart";
    }

    @GetMapping("/chart/data")
    @ResponseBody // json으로 데이터를 전달하기 위해 필요
    public List<ChartDto> getOrderCounts() {

        return chartService.orderCount();
    }
}
