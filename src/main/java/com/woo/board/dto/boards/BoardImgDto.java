package com.woo.board.dto.boards;

import com.woo.board.dto.items.ItemImgDto;
import com.woo.board.entity.boards.BoardImg;
import com.woo.board.entity.items.ItemImg;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class BoardImgDto {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private static ModelMapper modelMapper = new ModelMapper();

    public static BoardImgDto of(BoardImg boardImg) {
        return modelMapper.map(boardImg, BoardImgDto.class);
    }
}
