package aivle.dog.domain.user.repository;

import aivle.dog.domain.user.dto.UserListResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;

import static aivle.dog.domain.user.entity.QUser.user;

@Log4j2
@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserListResponseDto> getUserList() {
        return queryFactory
                .select(Projections.constructor(UserListResponseDto.class,
                        user.username,
                        user.businessNumber,
                        user.companyName))
                .from(user)
                .orderBy(user.businessNumber.asc())
                .fetch();
    }
}
