package aivle.dog.domain.board.controller;

import aivle.dog.domain.board.dto.CommentDto;
import aivle.dog.domain.board.service.CommentService;
import aivle.dog.global.Message;
import aivle.dog.global.StateEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
@RequestMapping("api/v1/board/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Message> postComment(@RequestBody CommentDto commentDto, @AuthenticationPrincipal UserDetails user) {
        log.info("CommentController/postComment : " + commentDto);
        log.info("CommentController/postComment : " + user.getUsername());

        Message message = new Message();
        try {
            int code = commentService.postComment(commentDto, user.getUsername());
            message.setStatus(StateEnum.OK);
            message.setMessage("댓글 등록 성공");
            message.setData(String.valueOf(code));
        } catch (Exception e) {
            log.error("CommentController/postComment : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Message> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails user) {
        log.info("CommentController/deleteComment : " + commentId);
        log.info("CommentController/deleteComment : " + user.getUsername());

        Message message = new Message();
        try {
            int code = commentService.deleteComment(commentId,user.getUsername());
            message.setStatus(StateEnum.OK);
            message.setMessage("댓글 삭제 성공");
            message.setData(String.valueOf(code));
        } catch (Exception e) {
            log.error("CommentController/deleteComment : " + e.getMessage(), e);
            message.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

}
