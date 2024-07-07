package aivle.dog.domain.board.controller;

import aivle.dog.domain.board.dto.BoardDto;
import aivle.dog.domain.board.dto.BoardListResponseDto;
import aivle.dog.domain.board.dto.BoardResponseDto;
import aivle.dog.domain.board.service.BoardService;
import aivle.dog.domain.user.dto.CustomUserDetails;
import aivle.dog.global.Message;
import aivle.dog.global.StateEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
@RequestMapping("api/v1/board")
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<Message> postBoard(@RequestBody BoardDto boardDto, @AuthenticationPrincipal CustomUserDetails user) {
        log.info("BoardController/postBoard : " + boardDto);
        log.info("BoardController/postBoard : " + user.getUsername());
        Message message = new Message();

        try {
            int code = boardService.postBoard(boardDto, user.getUsername());
            message.setStatus(StateEnum.OK);
            message.setMessage("게시글 등록 성공");
            message.setData(String.valueOf(code));
        } catch (Exception e) {
            log.error("BoardController/postBoard : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Message> getBoard(@PathVariable("boardId") Long boardId) {
        log.info("BoardController/getBoard : " + boardId);

        Message message = new Message();

        try {
            BoardResponseDto boardResponseDto = boardService.getBoard(boardId);
            boardService.updateViewCount(boardId);
            message.setStatus(StateEnum.OK);
            message.setMessage("게시글 조회 성공");
            message.setData(boardResponseDto);
        } catch (Exception e) {
            log.error("BoardController/getBoard : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Message> patchBoard(@PathVariable Long boardId, @RequestBody BoardDto boardDto, @AuthenticationPrincipal CustomUserDetails user) {
        log.info("BoardController/patchBoard = boardDto : " + boardDto);
        log.info("BoardController/patchBoard = boardId : " + boardId);
        log.info("BoardController/patchBoard = user : " + user.getUsername());

        Message message = new Message();

        try {
            int code = boardService.patchBoard(boardId, boardDto, user.getUsername());
            message.setStatus(StateEnum.OK);
            message.setMessage("게시글 수정 성공");
            message.setData(String.valueOf(code));
        } catch (Exception e) {
            log.error("BoardController/patchBoard : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Message> deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal CustomUserDetails user) {
        log.info("BoardController/deleteBoard =  boardId : " + boardId);
        log.info("BoardController/deleteBoard =  user : " + user.getUsername());
        Message message = new Message();

        try {
            int code = boardService.deleteBoard(boardId, user.getUsername());
            message.setStatus(StateEnum.OK);
            message.setMessage("게시글 삭제 성공");
            message.setData(String.valueOf(code));
        } catch (Exception e) {
            log.error("BoardController/deleteBoard : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/list")
    public ResponseEntity<Message> getBoardList(Pageable pageable) {
        log.info("BoardController/getBoardList =  page : " + pageable);
        Message message = new Message();

        try {
            Page<BoardListResponseDto> paging = boardService.getBoardList(pageable);
            message.setStatus(StateEnum.OK);
            message.setMessage("게시글 리스트 조회 성공");
            message.setData(paging);
        } catch (Exception e) {
            log.error("BoardController/getBoardList : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }
}
