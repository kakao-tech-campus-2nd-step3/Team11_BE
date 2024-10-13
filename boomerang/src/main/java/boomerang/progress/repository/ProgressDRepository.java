package boomerang.progress.repository;

import boomerang.progress.domain.ProgressA;
import boomerang.progress.domain.ProgressD;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgressDRepository extends JpaRepository<ProgressD, Long> {
    Optional<ProgressD> findByMemberId(Long id);
}
