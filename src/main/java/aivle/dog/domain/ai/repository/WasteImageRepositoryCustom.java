package aivle.dog.domain.ai.repository;

import aivle.dog.domain.ai.dto.WasteListResponseDto;

import java.util.List;

public interface WasteImageRepositoryCustom {
    List<WasteListResponseDto> getWasteList();
}
