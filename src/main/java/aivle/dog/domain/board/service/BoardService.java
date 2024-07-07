package aivle.dog.domain.board.service;

import aivle.dog.domain.board.dto.BoardDto;
import aivle.dog.domain.board.dto.BoardListResponseDto;
import aivle.dog.domain.board.dto.BoardResponseDto;
import aivle.dog.domain.board.dto.CommentResponseDto;
import aivle.dog.domain.board.entity.Board;
import aivle.dog.domain.board.entity.InquiryType;
import aivle.dog.domain.board.repository.BoardRepository;
import aivle.dog.domain.board.repository.CommentRepository;
import aivle.dog.domain.user.entity.User;
import aivle.dog.domain.user.repository.UserRepository;
import aivle.dog.global.enums.EnumValueConvertUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Integer postBoard(BoardDto boardDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("없는 사용자입니다"));

        boardRepository.save(Board.builder()
                .inquiryType(EnumValueConvertUtils.ofDesc(InquiryType.class, boardDto.getInquiryType()))
                .title(boardDto.getTitle())
                .description(boardDto.getDescription())
                .viewCount(0L)
                .user(user).build());
        return 0;
    }

    @Transactional(readOnly = true)
    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("없는 게시글입니다"));
        List<CommentResponseDto> comments = commentRepository.getCommentsByBoardId(boardId);
        return BoardResponseDto.builder()
                .title(board.getTitle())
                .description(board.getDescription())
                .username(board.getUser().getUsername())
                .createdAt(board.getCreatedAt())
                .viewCount(board.getViewCount() + 1)
                .comments(comments)
                .build();
    }

    @Transactional
    public int patchBoard(Long boardId, BoardDto boardDto, String username) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("없는 게시글입니다"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("없는 사용자입니다"));

        if (!board.getUser().getUsername().equals(user.getUsername()))
            throw new RuntimeException("작성자와 수정자가 다릅니다");

        boardRepository.save(board.updateBoard(boardDto.getTitle(), boardDto.getDescription()));

        return 0;
    }

    @Transactional
    public int deleteBoard(Long boardId, String username) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("없는 게시글입니다"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("없는 사용자입니다"));

        if (!board.getUser().getUsername().equals(user.getUsername()))
            throw new RuntimeException("작성자와 삭제자가 다릅니다");

        boardRepository.deleteById(boardId);

        return 0;
    }

    @Transactional(readOnly = true)
    public Page<BoardListResponseDto> getBoardList(Pageable pageable) {
        return boardRepository.getBoardList(pageable);
    }

    @Transactional
    public void updateViewCount(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("없는 게시글입니다"));
        boardRepository.save(board.updateViewCount(board.getViewCount()));
    }
}
