package boomerang.board.service;

import boomerang.board.domain.Board;
import boomerang.board.exception.BoardNotFoundException;
import boomerang.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public Board getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);
    }

    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    public Board updateBoard(Board board) {
        validateBoardExists(board.getId());
        return boardRepository.save(board);
    }

    public void deleteBoard(Long id) {
        validateBoardExists(id);
        boardRepository.deleteById(id);
    }

    private void validateBoardExists(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new BoardNotFoundException();
        }
    }
}
