package aivle.dog.domain.board.service;

import aivle.dog.domain.board.dto.CommentDto;
import aivle.dog.domain.board.entity.Board;
import aivle.dog.domain.board.entity.Comment;
import aivle.dog.domain.board.repository.BoardRepository;
import aivle.dog.domain.board.repository.CommentRepository;
import aivle.dog.domain.user.entity.Admin;
import aivle.dog.domain.user.entity.User;
import aivle.dog.domain.user.repository.AdminRepository;
import aivle.dog.domain.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
public class CommentService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final CommentRepository commentRepository;

    public CommentService(BoardRepository boardRepository, UserRepository userRepository, AdminRepository adminRepository, CommentRepository commentRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Integer postComment(CommentDto commentDto, String username) {
        Board board = boardRepository.findById(commentDto.getBoardId())
                .orElseThrow(() -> new RuntimeException("없는 게시글입니다"));

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            // 관리자가 작성하는 경우
            Admin admin = adminRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("없는 사용자입니다"));
            commentRepository.save(Comment.builder()
                    .name(admin.getName())
                    .description(commentDto.getDescription())
                    .board(board)
                    .admin(admin)
                    .build());
        } else {
            // 사용자가 작성하는 경우
            commentRepository.save(Comment.builder()
                    .name(user.get().getCompanyName())
                    .description(commentDto.getDescription())
                    .board(board)
                    .user(user.get())
                    .build());
        }
        return 0;
    }

    @Transactional
    public int deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("없는 댓글입니다"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("없는 사용자입니다"));

        if (!comment.getUser().getUsername().equals(user.getUsername()))
            throw new RuntimeException("작성자와 삭제자가 다릅니다");

        commentRepository.deleteById(commentId);

        return 0;
    }
}
