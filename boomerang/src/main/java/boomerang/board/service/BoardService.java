package boomerang.board.service;

import boomerang.board.domain.BoardDomain;
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

    public List<BoardDomain> getAllTemplateDomains() {
        return boardRepository.findAll();
    }

    public BoardDomain getTemplateDomainById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);
    }

    public BoardDomain createTemplateDomain(BoardDomain boardDomain) {
        return boardRepository.save(boardDomain);
    }

    public BoardDomain updateTemplateDomain(BoardDomain boardDomain) {
        validateTemplateDomainExists(boardDomain.getId());
        return boardRepository.save(boardDomain);
    }

    public void deleteTemplateDomain(Long id) {
        validateTemplateDomainExists(id);
        boardRepository.deleteById(id);
    }

    private void validateTemplateDomainExists(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new BoardNotFoundException();
        }
    }
}
