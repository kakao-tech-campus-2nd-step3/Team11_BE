package boomerang.member.repository;

import boomerang.member.domain.Member;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsById(Long id);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname); // 닉네임 중복 검사를 위해서
    Optional<Member> findByEmail(String email);
}
