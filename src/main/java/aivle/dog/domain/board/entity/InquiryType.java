package aivle.dog.domain.board.entity;

import aivle.dog.global.enums.EnumValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryType implements EnumValue<Integer> {
    ACCOUNTS("계정", 1),
    USE_OF_SERVICE("서비스 이용", 2),
    ETC("기타", 3);

    private final String desc;
    private final Integer dbValue;
}
