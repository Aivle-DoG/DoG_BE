package aivle.dog.domain.board.service;

import aivle.dog.domain.board.dto.BoardDto;
import aivle.dog.domain.board.dto.BoardResponseDto;
import aivle.dog.domain.board.entity.Board;
import aivle.dog.domain.board.repository.BoardRepository;
import aivle.dog.domain.user.entity.User;
import aivle.dog.domain.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Integer postBoard(BoardDto boardDto) {
        User user = userRepository.findByUsername(boardDto.getUsername())
                .orElseThrow(() -> new RuntimeException("없는 사용자입니다"));

        boardRepository.save(Board.builder()
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
        return BoardResponseDto.builder()
                .title(board.getTitle())
                .description(board.getDescription())
                .username(board.getUser().getUsername())
                .modifiedAt(board.getModifiedAt())
                .viewCount(board.getViewCount())
                .build();
    }

    @Transactional
    public int patchBoard(Long boardId, BoardDto boardDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("없는 게시글입니다"));

        User user = userRepository.findByUsername(boardDto.getUsername())
                .orElseThrow(() -> new RuntimeException("없는 사용자입니다"));

        if (!board.getUser().getUsername().equals(user.getUsername()))
            throw new RuntimeException("작성자와 수정자가 다릅니다");

        boardRepository.save(board.updateBoard(boardDto.getTitle(), boardDto.getDescription()));

        return 0;
    }

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

    public void updateViewCount(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("없는 게시글입니다"));
        boardRepository.save(board.updateViewCount(board.getViewCount()));
    }
}
