package aivle.dog.domain.board.repository;

import aivle.dog.domain.board.dto.CommentResponseDto;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentResponseDto> getCommentsByBoardId(Long boardId);
}
