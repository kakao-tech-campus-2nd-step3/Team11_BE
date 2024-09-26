package boomerang.like.repository;

import boomerang.global.MyCrudRepository;
import boomerang.like.domain.LikeDomain;
import java.util.List;

public interface LikeRepository extends MyCrudRepository<LikeDomain, Long> {
    boolean existsById(Long id);

    List<LikeDomain> findByBoardId(Long boardId);

    boolean existsByMemberIdAndBoardId(Long memberId, Long boardId);

    LikeDomain findByMemberIdAndBoardId(Long memberId, Long boardId);

    void delete(LikeDomain like);
}
