package boomerang.domain.member.repository;

import boomerang.domain.member.domain.MemberDomain;
import boomerang.global.MyCrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberDomain, Long> {
    boolean existsById(Long id);
}
