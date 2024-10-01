package boomerang.board.service;

import boomerang.board.domain.Board;
import boomerang.board.dto.BoardListRequestDto;
import boomerang.board.repository.BoardRepository;
import boomerang.domain.member.domain.Member;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
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

    // 모든 게시물 가져오기
    public List<Board> getAllBoards(BoardListRequestDto boardListRequestDto) {
        PageRequest pageRequest = PageRequest.of(
                boardListRequestDto.getPage(),
                boardListRequestDto.getSize(),
                Sort.by(boardListRequestDto.getSortDirection(), boardListRequestDto.getSortBy())
        );
        Page<Board> boardPage = boardRepository.findAll(pageRequest);
        return boardPage.getContent();
    }

    // ID로 게시물 가져오기
    public Board getBoardById(Long id) {
        return validateBoardExists(id);
    }

    // 게시물 생성
    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    // 게시물 업데이트
    public Board updateBoard(Board board) {
        validateBoardOwnership(board.getMember(), board.getId());
        return boardRepository.save(board);
    }

    // 게시물 삭제
    public void deleteBoard(Member member, Long id) {
        validateBoardOwnership(member, id);
        boardRepository.deleteById(id);
    }

    // 게시물 존재 여부 검증
    private Board validateBoardExists(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND_ERROR));
    }

    // 게시물 소유자 검증
    private void validateBoardOwnership(Member member, Long id) {
        Board board = validateBoardExists(id);
        if (!board.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.BOARD_DONT_HAS_OWNERSHIP_ERROR);
        }
    }
}

