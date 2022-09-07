package com.woo.board.repository.boards;

import com.woo.board.entity.boards.Boards;
import com.woo.board.entity.boards.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByBoard(Boards board);
}
