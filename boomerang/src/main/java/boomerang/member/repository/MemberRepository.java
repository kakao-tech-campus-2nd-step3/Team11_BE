package boomerang.member.repository;

import boomerang.member.domain.Member;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsById(Long id);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname); // 닉네임 중복 검사를 위해서
//    // 특정 닉네임 패턴을 가진 닉네임 중에서 가장 큰 숫자를 찾는 메서드
//    @Query("SELECT MAX(CAST(SUBSTRING(m.nickname, LENGTH(:baseNickname) + 1) AS int)) " +
//        "FROM Member m WHERE m.nickname LIKE CONCAT(:baseNickname, '%') AND " +
//        "SUBSTRING(m.nickname, LENGTH(:baseNickname) + 1) REGEXP '^[0-9]+$'")
//    Integer findMaxSuffixByNicknamePattern(String baseNickname);
    Optional<Member> findByEmail(String email);
}
