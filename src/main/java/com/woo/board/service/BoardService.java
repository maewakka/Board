package com.woo.board.service;

import com.woo.board.config.auth.SessionUser;
import com.woo.board.dto.boards.BoardFormDto;
import com.woo.board.dto.boards.BoardSearchDto;
import com.woo.board.entity.boards.Boards;
import com.woo.board.repository.boards.BoardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardsRepository boardsRepository;
    private final HttpSession httpSession;

    // 신규 게시글 저장
    @Transactional
    public Boards newWrite(BoardFormDto boardFormDto) {
        return boardsRepository.save(boardFormDto.toEntity());
    }

    // 공지사항 메인페이지 내용 불러오기
    @Transactional
    public Page<Boards> getBoardsListPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        return boardsRepository.getBoardsPage(boardSearchDto, pageable);
    }

    // 선택한 게시글 내용 불러오기
    @Transactional(readOnly = true)
    public BoardFormDto getBoardDtl(Long boardId) {
        Boards board = boardsRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        BoardFormDto boardFormDto = BoardFormDto.of(board);
        return boardFormDto;
    }

    // 게시글 수정된 내용 저장
    public Long updateBoard(BoardFormDto boardFormDto) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null) {
            Boards board = boardsRepository.findById(boardFormDto.getId())
                    .orElseThrow(EntityNotFoundException::new);
            if(board.getCreatedBy().equals(user.getEmail())) {
                board.updateBoard(boardFormDto);
                return board.getId();
            }
        }
        return null;
    }

}
