package aivle.dog.domain.board.repository;

import aivle.dog.domain.board.dto.CommentResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;

import static aivle.dog.domain.board.entity.QComment.comment;

@Log4j2
@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentResponseDto> getCommentsByBoardId(Long boardId) {
        // 댓글 테이블의 boardId에 해당하는 댓글들을 수정일자를 기준으로 오름차순으로 정렬해서 리턴
        return queryFactory
                .select(Projections.constructor(CommentResponseDto.class,
                        comment.name,
                        comment.description,
                        comment.modifiedAt))
                .from(comment)
                .where(comment.board.id.eq(boardId))
                .orderBy(comment.modifiedAt.asc())
                .fetch();
    }
}
