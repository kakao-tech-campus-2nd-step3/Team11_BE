package boomerang.like.repository;

import boomerang.board.domain.Board;
import boomerang.like.domain.Like;
import boomerang.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByMemberAndBoardAndIsDeletedFalse(Member loginMember, Board board);

    Optional<Like> findByMemberAndBoardAndIsDeletedFalse(Member member, Board board);

    int countByBoardIdAndIsDeletedFalse(Long id);
}
