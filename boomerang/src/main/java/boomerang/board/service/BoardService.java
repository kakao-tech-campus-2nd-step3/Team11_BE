package boomerang.board.service;

import boomerang.board.domain.Board;
import boomerang.board.dto.BoardListRequestDto;
import boomerang.board.exception.BoardNotFoundException;
import boomerang.board.repository.BoardRepository;
import boomerang.domain.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> getAllBoards(BoardListRequestDto boardListRequestDto) {

        PageRequest pageRequest = PageRequest.of(
                boardListRequestDto.getPage(),
                boardListRequestDto.getSize(),
                Sort.by(boardListRequestDto.getSortDirection(), boardListRequestDto.getSortBy())
        );

        Page<Board> boardPage = boardRepository.findAll(pageRequest);

        return boardPage.getContent();
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
