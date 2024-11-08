package boomerang.board.repository;

import boomerang.board.domain.Board;
import boomerang.board.domain.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByBoardType(BoardType boardType, Pageable pageable);
}