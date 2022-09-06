package com.woo.board.service;

import com.woo.board.config.auth.SessionUser;
import com.woo.board.dto.items.ItemRegFormDto;
import com.woo.board.dto.items.ItemSearchDto;
import com.woo.board.dto.items.ItemViewDto;
import com.woo.board.dto.items.ItemsDto;
import com.woo.board.entity.items.ItemImg;
import com.woo.board.entity.items.Items;
import com.woo.board.repository.items.ItemImgRepository;
import com.woo.board.repository.items.ItemsRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final HttpSession httpSession;

    @Value("${itemImgLocation}")
    private String itemImgLocation;
    private final ItemsRepository itemsRepository;
    private final FileService fileService;
    private final ItemImgRepository itemImgRepository;

    @Transactional
    public Long newRegister(ItemRegFormDto itemRegFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        Items item = itemsRepository.save(itemRegFormDto.createItem());

        for(int i=0; i<itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItems(item);
            if(i == 0) {
                itemImg.setRepimgYn("Y");
            }
            else {
                itemImg.setRepimgYn("N");
            }
            this.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if(!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    @Transactional(readOnly = true)
    public Page<ItemsDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemsRepository.getItemsPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Items> getRegItemsListPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        String userEmail = user.getEmail();

        return itemsRepository.getRegItemsPage(itemSearchDto, pageable, userEmail);
    }

    @Transactional(readOnly = true)
    public ItemViewDto getItem(Long itemId) {
        Items item = itemsRepository.findById(itemId).get();
        List<ItemImg> itemImgList = itemImgRepository.findByItems(item);
        List<String> imgUrlList = new ArrayList<>();

        for(int i=0; i<itemImgList.size(); i++) {
            String imgUrl = itemImgList.get(i).getImgUrl();
            if(!"".equals(imgUrl)) {
                imgUrlList.add(imgUrl);
            }
        }
        return new ItemViewDto(item, imgUrlList);
    }

}
