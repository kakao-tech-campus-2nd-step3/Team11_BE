package boomerang.board.repository;

import boomerang.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b WHERE b.createdAt >= :startDate " +
            "ORDER BY (b.likeCount + b.commentCount * :weight) DESC")
    Page<Board> findBestBoardsByDateAndScore(@Param("startDate") LocalDate startDate,
                                             @Param("weight") Double weight,
                                             Pageable pageable);
}
