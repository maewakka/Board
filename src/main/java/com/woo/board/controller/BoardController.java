package com.woo.board.controller;

import com.woo.board.config.auth.LoginUser;
import com.woo.board.config.auth.SessionUser;
import com.woo.board.dto.boards.BoardFormDto;
import com.woo.board.dto.boards.BoardSearchDto;
import com.woo.board.dto.boards.ChatFormDto;
import com.woo.board.dto.boards.ChatListDto;
import com.woo.board.entity.boards.Boards;
import com.woo.board.entity.boards.Chat;
import com.woo.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log
public class BoardController {

    private final BoardService boardService;
    private RequestCache requestCache = new HttpSessionRequestCache();

    // 공지사항 메인 화면 리스트를 페이지 형식으로 출력
    @GetMapping(value = {"/boards/notice", "/boards/notice/{page}"})
    public String noticeMain(BoardSearchDto boardSearchDto, @PathVariable("page")Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
        Page<Boards> boards = boardService.getBoardsListPage(boardSearchDto, pageable);
        model.addAttribute("boards", boards);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 5);

        return "boards/boardList";
    }

    // 글쓰기 화면 이동
    @GetMapping("/boards/notice/write")
    public String noticeWrite(Model model) {
        model.addAttribute("boardFormDto", new BoardFormDto());
        return "boards/write";
    }

    // 글쓰기 화면에서 작성한 내용 저장
    @PostMapping("/boards/notice/write")
    public String board_write(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, Model model,
                              @RequestPart("boardImgFile") List<MultipartFile> boardImgFileList) {
        if(bindingResult.hasErrors()) {
            return "boards/write";
        }
        try {
            boardService.newWrite(boardFormDto, boardImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "글 등록 중 에러가 발생하였습니다.");
            return "boards/write";
        }

        return "redirect:/boards/notice";
    }

    // 게시글 보는 페이지 이동
    @GetMapping("/boards/notice/view/{boardId}")
    public String boardDtl(@PathVariable("boardId")Long boardId, ChatFormDto chatFormDto, Model model) {
        model.addAttribute("chatFormDto", chatFormDto);
        try {
            BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
            List<ChatListDto> chatListDtoList = boardService.getChatList(boardId);
            model.addAttribute("chatList", chatListDtoList);
            model.addAttribute("boardFormDto", boardFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 글입니다.");
            model.addAttribute("boardFormDto", new BoardFormDto());

            return "boards/view";
        }

        return "boards/view";
    }

    // 게시글 수정 페이지 이동
    @GetMapping("/boards/notice/update/{boardId}")
    public String boardUpdateDtl(@PathVariable("boardId")Long boardId, Model model) {
        try {
            BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
            model.addAttribute("boardFormDto", boardFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 글입니다.");
            model.addAttribute("boardFormDto", new BoardFormDto());
            return "boards/write";
        }
        return "boards/write";
    }

    // 게시글 수정한 내용 저장
    @PostMapping("/boards/notice/update/{boardId}")
    public String updateBoard(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, @PathVariable("boardId") Long boardId, Model model) {

        if(bindingResult.hasErrors()) {
            return "boards/write";
        }
        try {
            boardService.updateBoard(boardFormDto);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "글 수정 중 에러가 발생하였습니다.");
            return "boards/write";
        }

        return "redirect:/boards/notice/view/" + boardId;
    }

    @PostMapping("/boards/chat/add")
    public String addChat(ChatFormDto chatFormDto) {
        boardService.addChat(chatFormDto);
        return "redirect:/boards/notice/view/" + chatFormDto.getBoardId();
    }

}
