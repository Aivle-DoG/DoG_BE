package aivle.dog.domain.board.repository;

import aivle.dog.domain.board.dto.BoardListResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static aivle.dog.domain.board.entity.QBoard.board;

@Log4j2
@RequiredArgsConstructor
@Repository
public class BoardRepositoryImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardListResponseDto> getBoardList(Pageable pageable) {
        List<BoardListResponseDto> boardListResponseDtoList = queryFactory
                .select(Projections.constructor(BoardListResponseDto.class,
                        board.id,
                        board.inquiryType,
                        board.title,
                        board.createdAt,
                        board.viewCount))
                .from(board)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 게시글 수를 구하는 쿼리
        long total = queryFactory
                .select(Wildcard.count)
                .from(board)
                .fetchOne();

        return new PageImpl<>(boardListResponseDtoList, pageable, total);
    }
}