package boomerang.like.repository;

import boomerang.global.MyCrudRepository;
import boomerang.like.domain.LikeDomain;

public interface LikeRepository extends MyCrudRepository<LikeDomain, Long> {
    boolean existsById(Long id);
}
