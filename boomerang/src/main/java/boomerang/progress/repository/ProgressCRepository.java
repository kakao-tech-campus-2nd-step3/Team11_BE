package boomerang.progress.repository;

import boomerang.progress.domain.ProgressA;
import boomerang.progress.domain.ProgressC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgressCRepository extends JpaRepository<ProgressC, Long> {
    Optional<ProgressC> findByMemberId(Long id);

}
