package boomerang.board.service;

import boomerang.board.domain.Board;
import boomerang.board.dto.BoardBestListRequestDto;
import boomerang.board.dto.BoardListRequestDto;
import boomerang.board.dto.BoardRequestDto;
import boomerang.board.dto.BoardResponseDto;
import boomerang.board.repository.BoardRepository;
import boomerang.comment.dto.CommentListResponseDto;
import boomerang.comment.repository.CommentRepository;
import boomerang.comment.service.CommentService;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 베스트 게시물 가져오기
    public Page<Board> getBestBoards(BoardBestListRequestDto boardBestListRequestDto) {
        PageRequest pageRequest = getBestPageRequest(boardBestListRequestDto);
        Page<Board> boardPage = boardRepository.findAll(pageRequest);
        return boardPage;
    }

    // 모든 게시물 가져오기
    public Page<Board> getAllBoards(BoardListRequestDto boardListRequestDto) {
        PageRequest pageRequest = getPageRequest(boardListRequestDto);
        Page<Board> boardPage = boardRepository.findAll(pageRequest);
        return boardPage;
    }

    // ID로 게시물 가져오기
    public Board getBoard(Long id) {
        return validateBoardExists(id);
    }

    // 게시물 생성
    public Board createBoard(BoardRequestDto boardRequestDto, Member member) {
        Board board = new Board(boardRequestDto, member);
        return boardRepository.save(board);
    }

    // 게시물 업데이트
    public Board updateBoard(Long id, BoardRequestDto boardRequestDto, Member member) {
        Board board = new Board(id, boardRequestDto, member);
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

    private PageRequest getBestPageRequest(BoardBestListRequestDto boardBestListRequestDto) {
        return PageRequest.of(
                0,
                boardBestListRequestDto.getSize(),
                Sort.by(Sort.Direction.DESC, "likeCount")
        );
    }

    private PageRequest getPageRequest(BoardListRequestDto boardListRequestDto) {
        return PageRequest.of(
                boardListRequestDto.getPage(),
                boardListRequestDto.getSize(),
                Sort.by(boardListRequestDto.getSort_direction(), boardListRequestDto.getSort_by())
        );
    }
}

