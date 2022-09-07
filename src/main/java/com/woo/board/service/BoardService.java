package com.woo.board.service;

import com.woo.board.config.auth.SessionUser;
import com.woo.board.dto.boards.*;
import com.woo.board.entity.boards.BoardImg;
import com.woo.board.entity.boards.Boards;
import com.woo.board.entity.boards.Chat;
import com.woo.board.entity.items.ItemImg;
import com.woo.board.entity.users.User;
import com.woo.board.repository.boards.BoardImgRepository;
import com.woo.board.repository.boards.BoardsRepository;
import com.woo.board.repository.boards.ChatRepository;
import com.woo.board.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardsRepository boardsRepository;
    private final HttpSession httpSession;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final BoardImgRepository boardImgRepository;
    private final ChatRepository chatRepository;
    @Value("${boardImgLocation}")
    private String boardImgLocation;

    // 신규 게시글 저장
    @Transactional
    public Long newWrite(BoardFormDto boardFormDto, List<MultipartFile> boardImgFileList) throws Exception{
        Boards board = boardsRepository.save(boardFormDto.toEntity());

        for(int i=0; i<boardImgFileList.size(); i++) {
            BoardImg boardImg = new BoardImg();
            boardImg.setBoard(board);
            this.saveBoardImg(boardImg, boardImgFileList.get(i));
        }

        return board.getId();
    }

    public void saveBoardImg(BoardImg boardImg, MultipartFile boardImgFile) throws Exception {
        String oriImgName = boardImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if(!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(boardImgLocation, oriImgName, boardImgFile.getBytes());
            imgUrl = "/images/board/" + imgName;
        }

        boardImg.updateItemImg(oriImgName, imgName, imgUrl);
        boardImgRepository.save(boardImg);
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
        List<BoardImg> boardImgs = boardImgRepository.findByBoard(board);
        List<String> imgUrlList = new ArrayList<>();

        for(int i=0; i<boardImgs.size(); i++) {
            String imgUrl = boardImgs.get(i).getImgUrl();
            if(!"".equals(imgUrl)) {
                imgUrlList.add(imgUrl);
            }
        }
        BoardFormDto boardFormDto = BoardFormDto.of(board);
        boardFormDto.setImgUrlList(imgUrlList);
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

    public Long addChat(ChatFormDto chatFormDto) {
        Boards board = boardsRepository.findById(chatFormDto.getBoardId()).orElseThrow(EntityNotFoundException::new);

        return chatRepository.save(Chat.builder()
                .content(chatFormDto.getContent())
                .board(board)
                .build()).getId();
    }

    @Transactional(readOnly = true)
    public List<ChatListDto> getChatList(Long boardId) {
        List<ChatListDto> chatListDtoList = new ArrayList<>();
        Boards board = boardsRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);

        List<Chat> chatList = chatRepository.findByBoard(board);
        for(int i=0; i<chatList.size(); i++) {
            Chat chat = chatList.get(i);
            User user = userRepository.findByEmail(chat.getCreatedBy()).orElseThrow(EntityNotFoundException::new);
            chatListDtoList.add(
                    ChatListDto.builder()
                            .createdDate(chat.getCreatedDate())
                            .userName(user.getName())
                            .content(chat.getContent())
                            .build()
            );
        }

        return chatListDtoList;
    }

}
