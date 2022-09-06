package com.woo.board.repository.boards;

import com.woo.board.entity.boards.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardsRepository extends JpaRepository<Boards, Long>, BoardsRepositoryCustom {
}
