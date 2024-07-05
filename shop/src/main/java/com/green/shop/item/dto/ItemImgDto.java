package com.green.shop.item.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemImgDto {

    private Long itemImgId;

    private String imgName;

    private String oriImgName; //원본 이미지 이름

    private String imgUrl; //이미지 경로

    private String repImgYn; //대표 이미지 확인

    private Long itemId; //외래키

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
