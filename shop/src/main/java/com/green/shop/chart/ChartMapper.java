package com.green.shop.chart;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChartMapper {

    List<ChartDto> orderCount();

}
