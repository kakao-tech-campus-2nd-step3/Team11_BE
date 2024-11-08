package boomerang.board.scheduler;

import boomerang.board.domain.Board;
import boomerang.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreScheduler {

    private final BoardRepository boardRepository;
    private final int VALID_DATE = 7;
    private final Long LIKE_WEIGHT = 10L;
    private final Long COMMENT_WEIGHT = 7L;

    public ScoreScheduler(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 4시간마다 실행 (밀리초 단위: 4 * 60 * 60 * 1000)
    @Transactional
    @Scheduled(fixedRate = 14400000)
    public void updateBoardScores() {
        List<Board> boards = boardRepository.findAll();

        // 각 Board의 score를 계산
        boards.forEach(board -> board.calculateScore(VALID_DATE, LIKE_WEIGHT, COMMENT_WEIGHT));

        // 모든 Board 객체를 한 번에 저장
        boardRepository.saveAll(boards);
    }
}