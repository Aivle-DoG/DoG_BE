package aivle.dog.domain.ai.repository;

import aivle.dog.domain.ai.entity.WasteImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteImageRepository extends JpaRepository<WasteImage, Long>, WasteImageRepositoryCustom {
}
