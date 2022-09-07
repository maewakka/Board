package com.woo.board.repository.boards;

import com.woo.board.entity.boards.BoardImg;
import com.woo.board.entity.boards.Boards;
import com.woo.board.entity.items.ItemImg;
import com.woo.board.entity.items.Items;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {
    List<BoardImg> findByBoard(Boards board);
}
