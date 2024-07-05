package com.green.shop.chart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartService {

    private final ChartMapper chartMapper;

    public List<ChartDto> orderCount(){
        return chartMapper.orderCount();
    }
}
