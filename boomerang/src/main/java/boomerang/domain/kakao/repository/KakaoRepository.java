package boomerang.domain.kakao.repository;

import boomerang.domain.kakao.domain.KakaoDomain;
import boomerang.global.MyCrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoRepository extends JpaRepository<KakaoDomain, Long> {
    boolean existsById(Long id);
}
