package boomerang.comment.repository;

import boomerang.comment.domain.Comment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.board.id = :boardId AND c.isDeleted = 'NOT_DELETED'")
    Page<Comment> findAllByBoardId(PageRequest pageRequest, @Param("boardId") Long boardId);

    @Query("SELECT c FROM Comment c WHERE c.id = :id AND c.isDeleted = 'NOT_DELETED'")
    Optional<Comment> findActiveById(@Param("id") Long id);

}

