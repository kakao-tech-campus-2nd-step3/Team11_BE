package boomerang.board.service;

import boomerang.board.domain.Board;
import boomerang.board.dto.BoardBestListRequestDto;
import boomerang.board.dto.BoardListRequestDto;
import boomerang.board.dto.BoardRequestDto;
import boomerang.board.repository.BoardRepository;
import boomerang.file.service.FileService;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final Double WEIGHT = 0.7;
    private final int MIN_DATE = 7;

    public BoardService(BoardRepository boardRepository, FileService fileService) {
        this.boardRepository = boardRepository;
        this.fileService = fileService;
    }

    // 베스트 게시물 가져오기
    public Page<Board> getBestBoards(BoardBestListRequestDto boardBestListRequestDto) {
        // x일 이내의 시작 날짜 계산
        LocalDate startDate = LocalDate.now().minusDays(MIN_DATE);

        // PageRequest 생성
        PageRequest pageRequest = PageRequest.of(
                0, boardBestListRequestDto.getSize(), Sort.unsorted());

        Page<Board> boardPage =  boardRepository.findBestBoardsByDateAndScore(startDate.atStartOfDay(), WEIGHT, boardBestListRequestDto.getBoard_type(), pageRequest);
        return boardPage;
    }


    // 모든 게시물 가져오기
    public Page<Board> getAllBoards(BoardListRequestDto boardListRequestDto) {
        PageRequest pageRequest = getPageRequest(boardListRequestDto);
        return boardRepository.findByBoardType(boardListRequestDto.getBoard_type(), pageRequest);
    }

    // ID로 게시물 가져오기
    public Board getBoard(Long id) {
        return validateBoardExists(id);
    }

    // 게시물 생성
    public Board createBoard(BoardRequestDto boardRequestDto, Member member, List<MultipartFile> images) {
        // S3에 이미지 업로드 및 URL 리스트 생성
        List<URL> imageUrls = uploadImages(member, images);

        insertImageUrlsIntoContent(boardRequestDto, imageUrls);

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

    private PageRequest getPageRequest(BoardListRequestDto boardListRequestDto) {
        return PageRequest.of(
                boardListRequestDto.getPage(),
                boardListRequestDto.getSize(),
                Sort.by(boardListRequestDto.getSort_direction(), boardListRequestDto.getSort_by())
        );
    }

    private List<URL> uploadImages(Member member, List<MultipartFile> images) {
        if (images.isEmpty())
            return Collections.emptyList();

        return images.stream()
                .map(image -> fileService.upload(member.getEmail(), image))  // 업로드 후 URL 반환
                .toList();
    }

    private void insertImageUrlsIntoContent(BoardRequestDto boardRequestDto, List<URL> imageUrls) {
        String content = boardRequestDto.getContent();

        // <img src="?" /> 의 ? 를 imageUrl로 대체
        for (URL imageUrl : imageUrls) {
            content = content.replaceFirst("<img src=\"\\?\" />", "<img src=\"" + imageUrl.toString() + "\" />");
        }

        boardRequestDto.setContentWithImageUrl(content);
    }
}

