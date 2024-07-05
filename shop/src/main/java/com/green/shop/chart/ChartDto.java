package com.green.shop.chart;

import lombok.Data;

import java.util.Date;

@Data
public class ChartDto {
    private Date orderDate;
    private  int count;
}
