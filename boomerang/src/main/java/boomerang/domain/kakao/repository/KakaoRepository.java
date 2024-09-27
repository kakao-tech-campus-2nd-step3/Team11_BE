package boomerang.domain.kakao.repository;

import boomerang.domain.kakao.domain.KakaoDomain;
import boomerang.global.MyCrudRepository;

public interface KakaoRepository extends MyCrudRepository<KakaoDomain, Long> {
    boolean existsById(Long id);
}
