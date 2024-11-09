package boomerang.progress.repository;


import boomerang.progress.domain.SubStepEnum;
import boomerang.progress.domain.SubStepInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubStepInfoRepository extends JpaRepository<SubStepInfo, Long> {
    Optional<SubStepInfo> findBySubStepEnum(SubStepEnum subStepEnum);
}
