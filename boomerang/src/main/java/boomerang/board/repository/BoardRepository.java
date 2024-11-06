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

//    @Query("SELECT b FROM Board b WHERE b.createdAt >= :startDate AND b.boardType = :boardType " +
//            "ORDER BY (b.likeCount + b.commentCount * :weight) DESC")
//    //createdAt는 LocalDateTime ,
//    Page<Board> findBestBoardsByDateAndScore(@Param("startDate") LocalDate startDate,
//                                             @Param("weight") Double weight,
//                                             @Param("boardType") BoardType boardType,
//                                             Pageable pageable);

    @Query("SELECT b FROM Board b WHERE b.createdAt >= :startDate AND b.boardType = :boardType " +
            "ORDER BY (b.likeCount + b.commentCount * COALESCE(:weight)) DESC")
    //COALESCE(:weight) 이렇게 하면 null 인경우 null 로 그냥 들어가요! 위에서는 아마 null 인경우 그냥 에러 반환하는 것 같고..
    Page<Board> findBestBoardsByDateAndScore(@Param("startDate") LocalDateTime startDate,
                                             @Param("weight") Double weight,
                                             @Param("boardType") BoardType boardType,
                                             Pageable pageable);



    Page<Board> findByBoardType(BoardType boardType, Pageable pageable);
}