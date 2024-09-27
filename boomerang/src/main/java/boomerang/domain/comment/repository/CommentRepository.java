package boomerang.domain.comment.repository;

import boomerang.domain.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    // 보드 ID에 따른 댓글을 createdAt 기준으로 내림차순 정렬
    Page<Comment> findAllByBoardIdOrderByCreatedAtDesc(Pageable pageable, Long boardId);

}
