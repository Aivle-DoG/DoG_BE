package aivle.dog.domain.board.repository;

import aivle.dog.domain.board.dto.BoardListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<BoardListResponseDto> getBoardList(Pageable pageable);
}
