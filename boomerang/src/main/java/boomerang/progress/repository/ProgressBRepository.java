package boomerang.progress.repository;

import boomerang.progress.domain.ProgressA;
import boomerang.progress.domain.ProgressB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgressBRepository extends JpaRepository<ProgressB, Long> {

    Optional<ProgressB> findByMemberId(Long id);

}
