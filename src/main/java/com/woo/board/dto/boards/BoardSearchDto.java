package com.woo.board.dto.boards;

import lombok.Data;

@Data
public class BoardSearchDto {

    private String searchDateType; // 조회시간 기준(all, 1d, 1w, 1m, 6m [d:일, w:주, m:월])
    private String searchBy; // 조회할 때 어떤 유형으로 조회할지(작성자, 제목, 내용)
    private String searchQuery; // 검색할 내용

}
