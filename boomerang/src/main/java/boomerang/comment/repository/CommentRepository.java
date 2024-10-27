package boomerang.comment.repository;

import boomerang.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    // 보드 ID에 따른 댓글을 createdAt 기준으로 내림차순 정렬 & 삭제되지않은 값만
    @Query("SELECT c FROM Comment c WHERE c.board.id = :boardId AND c.isDeleted = 'NOT_DELETED' ORDER BY c.createdAt DESC")
    Page<Comment> findAllByBoardIdAndIsDeletedNot(Pageable pageable, @Param("boardId") Long boardId);


}
