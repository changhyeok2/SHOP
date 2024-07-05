package com.green.shop.item.service;

import com.green.shop.item.dto.ItemImgDto;
import com.green.shop.item.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final FileService fileService;
    private final ItemMapper itemMapper;

    //이미지 등록
    public void saveItemImg(ItemImgDto itemImgDto,
                            MultipartFile itemImgFile) throws Exception{

        //원본 파일명 저장
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        //사용자가 상품이미지를 등록했으면 uploadFile메서드 호출
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation,
                                    oriImgName, itemImgFile.getBytes());

            //저장한 상품 이미지를 불러올 경로를 설정
            imgUrl = "/images/item/" + imgName;
        }

        //상품 이미지 정보 저장
        itemImgDto.setImgName(imgName);
        itemImgDto.setOriImgName(oriImgName);
        itemImgDto.setImgUrl(imgUrl);

        //sql 실행
        itemMapper.insertItemImg(itemImgDto);
    }

    //이미지 변경
    public void updateItemImg(Long itemImgId,
                      MultipartFile itemImgFile) throws Exception{

        if (!itemImgFile.isEmpty()) {
            ItemImgDto savedItemImg = itemMapper.selectItemImgId(itemImgId);

            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(
                        itemImgLocation + "/" + savedItemImg.getImgName());

            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(
                    itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/"+imgName;

            savedItemImg.setOriImgName(oriImgName);
            savedItemImg.setImgName(imgName);
            savedItemImg.setImgUrl(imgUrl);

            itemMapper.updateItemImg(savedItemImg);

        }


    }
}
