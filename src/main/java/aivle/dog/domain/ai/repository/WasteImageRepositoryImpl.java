package aivle.dog.domain.ai.repository;

import aivle.dog.domain.ai.dto.WasteListResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;

import static aivle.dog.domain.ai.entity.QWasteImage.wasteImage;

@Log4j2
@RequiredArgsConstructor
@Repository
public class WasteImageRepositoryImpl implements WasteImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<WasteListResponseDto> getWasteList() {
        return queryFactory
                .select(Projections.constructor(WasteListResponseDto.class,
                        wasteImage.id,
                        wasteImage.user.companyName,
                        wasteImage.createdAt,
                        wasteImage.csv))
                .from(wasteImage)
                .orderBy(wasteImage.createdAt.asc())
                .fetch();
    }
}
