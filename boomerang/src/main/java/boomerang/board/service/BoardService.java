package boomerang.board.service;

import boomerang.board.domain.Board;
import boomerang.board.dto.BoardBestListRequestDto;
import boomerang.board.dto.BoardListRequestDto;
import boomerang.board.dto.BoardRequestDto;
import boomerang.board.repository.BoardRepository;
import boomerang.board.util.ContentImageProcessor;
import boomerang.file.service.FileService;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileService fileService;

    public BoardService(BoardRepository boardRepository, FileService fileService) {
        this.boardRepository = boardRepository;
        this.fileService = fileService;
    }

    // 베스트 게시물 가져오기
    public Page<Board> getBestBoards(BoardBestListRequestDto boardBestListRequestDto) {
        // 정렬을 score 기준으로 내림차순 설정
        PageRequest pageRequest =
            PageRequest.of(0, boardBestListRequestDto.getSize(),
                Sort.by(Sort.Direction.DESC, "score"));

        return boardRepository.findByBoardType(boardBestListRequestDto.getBoard_type(),
            pageRequest);
    }


    // 모든 게시물 가져오기
    public Page<Board> getAllBoards(BoardListRequestDto boardListRequestDto) {
        PageRequest pageRequest = getPageRequest(boardListRequestDto);

        return boardRepository.findByBoardTypeAndTitleContaining(
            boardListRequestDto.getBoard_type(), boardListRequestDto.getSearch_word(), pageRequest
        );
    }

    // ID로 게시물 가져오기
    public Board getBoard(Long id) {
        return validateBoardExists(id);
    }

    // 게시물 생성
    public Board createBoard(BoardRequestDto boardRequestDto, Member member,
        List<MultipartFile> images) {
        // S3에 이미지 업로드 및 URL 리스트 생성
        List<URL> imageUrls = uploadImages(member, images);

        ContentImageProcessor.insertImageUrlsIntoContent(boardRequestDto, imageUrls);

        Board board = new Board(boardRequestDto, member);

        return boardRepository.save(board);
    }

    // 게시물 업데이트
    public Board updateBoard(Long id, BoardRequestDto boardRequestDto, Member member,
        List<MultipartFile> images) {
        // S3에 이미지 업로드 및 URL 리스트 생성
        List<URL> imageUrls = uploadImages(member, images);

        ContentImageProcessor.insertImageUrlsIntoContent(boardRequestDto, imageUrls);

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

    private PageRequest getPageRequest(BoardListRequestDto boardListRequestDto) {
        return PageRequest.of(
            boardListRequestDto.getPage(),
            boardListRequestDto.getSize(),
            Sort.by(boardListRequestDto.getSort_direction(),
                boardListRequestDto.getBoard_sort_type().getName())
        );
    }

    private List<URL> uploadImages(Member member, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            return images.stream()
                .map(image -> fileService.upload(member.getEmail(), image))
                .toList();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.S3_UPLOAD_ERROR);
        }
    }
}

