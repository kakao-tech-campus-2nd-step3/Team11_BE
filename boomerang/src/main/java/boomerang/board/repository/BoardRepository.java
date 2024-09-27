package boomerang.board.repository;

import boomerang.board.domain.BoardDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardDomain, Long> {
}
