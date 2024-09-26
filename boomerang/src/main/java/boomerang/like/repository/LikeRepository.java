package boomerang.like.repository;

import boomerang.global.MyCrudRepository;
import boomerang.like.domain.LikeDomain;
import java.util.List;

public interface LikeRepository extends MyCrudRepository<LikeDomain, Long> {
    boolean existsById(Long id);

    List<LikeDomain> findByBoardId(Long boardId);
}
