package boomerang.like.repository;

import boomerang.board.domain.Board;
import boomerang.like.domain.Like;
import boomerang.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByMemberAndBoardAndIsDeletedFalse(Member loginMember, Board board);

    Optional<Like> findByMemberAndBoardAndIsDeletedFalse(Member member, Board board);

    int countByBoardIdAndIsDeletedFalse(Long id);
}
