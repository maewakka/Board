package com.woo.board.repository.boards;

import com.woo.board.dto.boards.BoardSearchDto;
import com.woo.board.entity.boards.Boards;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardsRepositoryCustom {

    Page<Boards> getBoardsPage(BoardSearchDto boardSearchDto, Pageable pageable);

}
