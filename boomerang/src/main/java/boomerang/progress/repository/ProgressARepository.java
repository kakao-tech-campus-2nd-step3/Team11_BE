package boomerang.progress.repository;

import boomerang.member.domain.Member;
import boomerang.progress.domain.ProgressA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgressARepository extends JpaRepository<ProgressA, Long>{

    Optional<ProgressA> findByMemberId(Long id);

}
