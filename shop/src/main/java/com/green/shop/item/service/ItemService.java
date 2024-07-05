package com.green.shop.item.service;

import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.dto.ItemImgDto;
import com.green.shop.item.dto.ItemMainDto;
import com.green.shop.item.form.ItemForm;
import com.green.shop.item.mapper.ItemMapper;
import jdk.jshell.spi.ExecutionControlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ItemService {

    private final ItemMapper itemMapper;
    private final ItemImgService itemImgService;


    public Long itemInsert(ItemForm itemForm, List<MultipartFile> itemImgFileList) throws Exception {

        ItemDto itemDto = makeItem(itemForm);

        itemMapper.itemInsert(itemDto);

        //이미지 등록
        for (int i=0; i<itemImgFileList.size(); i++) {
            ItemImgDto itemImgDto = new ItemImgDto();

            itemImgDto.setItemId(itemDto.getItemId());

            if (i==0)
                itemImgDto.setRepImgYn("Y");
            else
                itemImgDto.setRepImgYn("N");


            itemImgService.saveItemImg(itemImgDto, itemImgFileList.get(i));
            System.out.println("itemImgDto : " + itemImgDto);
        }

        return itemDto.getItemId();
    }

    public List<ItemDto> itemListAll() {
        return itemMapper.itemListAll();
    }


    public ItemForm getItemDtl(Long itemId) {
        //상품 조회
        ItemDto itemDto = itemMapper.selectItem(itemId);

        if (itemDto == null) {
            throw new NullPointerException("상품이 존재하지 않습니다.");
        }

        ItemForm itemForm = makeItemForm(itemDto);

        itemForm.setItemImgList(itemMapper.selectItemImg(itemId));

        return itemForm;
    }


    //ItemForm을 ItemDto로 변환
    private ItemDto makeItem(ItemForm itemForm) {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(itemForm.getId());
        itemDto.setItemName(itemForm.getItemName());
        itemDto.setPrice(itemForm.getPrice());
        itemDto.setStockNumber(itemForm.getStockNumber());
        itemDto.setItemDetail(itemForm.getItemDetail());
        itemDto.setItemSellStatus(itemForm.getItemSellStatus());

        return itemDto;
    }

    //ItemDto를 ItemForm으로 변환
    private ItemForm makeItemForm(ItemDto itemDto) {
        ItemForm itemForm = new ItemForm();
        itemForm.setId(itemDto.getItemId());
        itemForm.setItemName(itemDto.getItemName());
        itemForm.setPrice(itemDto.getPrice());
        itemForm.setStockNumber(itemDto.getStockNumber());
        itemForm.setItemDetail(itemDto.getItemDetail());
        itemForm.setItemSellStatus(itemDto.getItemSellStatus());

        return itemForm;
    }

    public Long updateItem(ItemForm itemForm,
                           List<MultipartFile> itemImgFileList)
                            throws Exception{

        ItemDto itemDto = makeItem(itemForm);

        int result = itemMapper.updateItem(itemDto);

        List<Long> itemImgIds = itemForm.getItemImgIds();

        for (int i=0; i<itemImgFileList.size(); i++) {
            ItemImgDto itemImgDto = new ItemImgDto();

            System.out.println("itemImgIds : " + itemImgIds);
            System.out.println("itemImgFileList : " + itemImgFileList);

            itemImgService.updateItemImg(itemImgIds.get(i),
                                            itemImgFileList.get(i));
        }
        return itemDto.getItemId();
    }

    public List<ItemDto> itemListPage(Map map){
        return itemMapper.itemListPage(map);
    }

    public int countAdminItems(Map map) {
        return itemMapper.countAdminItems(map);
    }

    //트랜잭션을 읽기전용으로 설정
    //insert, update, delete 작업을 실행하면 예외가 발생
    @Transactional(readOnly = true)
    public List<ItemMainDto> mainSelect(Map map){
        return itemMapper.mainSelect(map);
    }
}
