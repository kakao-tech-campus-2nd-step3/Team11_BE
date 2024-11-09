package boomerang.progress.repository;


import boomerang.progress.domain.SubStepEnum;
import boomerang.progress.domain.SubStepInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubStepInfoRepository extends JpaRepository<SubStepInfo, Long> {

    Optional<SubStepInfo> findBySubStepEnum(SubStepEnum subStepEnum);
}
