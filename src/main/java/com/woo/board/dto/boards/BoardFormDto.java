package com.woo.board.dto.boards;

import com.woo.board.dto.items.ItemImgDto;
import com.woo.board.entity.boards.BoardImg;
import com.woo.board.entity.boards.Boards;
import com.woo.board.entity.items.Items;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardFormDto {

    private Long id;

    @NotNull(message = "제목은 필수 입력 값입니다.")
    private String title;

    private String createdBy;

    @NotNull(message = "내용은 필수 입력 값입니다.")
    private String content;

    private List<String> imgUrlList;
    private List<BoardFormDto> boardImgDtoList = new ArrayList<>();

    private List<Long> boardImgIds = new ArrayList<>();
    private static ModelMapper modelMapper = new ModelMapper();

    public Boards toEntity() {
        return Boards.builder()
                .title(title)
                .content(content)
                .build();
    }

    public static BoardFormDto of(Boards board) {
        return modelMapper.map(board, BoardFormDto.class);
    }

}
