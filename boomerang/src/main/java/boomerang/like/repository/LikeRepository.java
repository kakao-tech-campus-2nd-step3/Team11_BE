package boomerang.like.repository;

import boomerang.board.domain.Board;
import boomerang.like.domain.Like;
import boomerang.member.domain.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Page<Like> findAllByBoardIdAndIsDeletedFalse(Long id, Pageable pageable);

    boolean existsByMemberAndBoardAndIsDeletedFalse(Member loginMember, Board board);
}
