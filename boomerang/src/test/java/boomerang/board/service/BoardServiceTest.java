package boomerang.board.service;

import boomerang.board.domain.Board;
import boomerang.board.domain.BoardType;
import boomerang.board.dto.BoardBestListRequestDto;
import boomerang.board.dto.BoardListRequestDto;
import boomerang.board.dto.BoardRequestDto;
import boomerang.board.repository.BoardRepository;
import boomerang.file.service.FileService;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.dto.MemberServiceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private BoardService boardService;

    private Board board;
    private Member member;
    private BoardRequestDto boardRequestDto;

    @BeforeEach
    void setUp() {
        member = new Member(new MemberServiceDto("user@example.com", "nickname"));
        boardRequestDto = new BoardRequestDto("Test Title", "Test Content", BoardType.ENTIRE, null);
        board = new Board(1L, boardRequestDto, member);
    }

    @Test
    void testGetBestBoards() {
        // given
        BoardBestListRequestDto requestDto = new BoardBestListRequestDto();
        requestDto.setSize(10);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "score"));
        Page<Board> pageResult = new PageImpl<>(List.of(board));

        given(boardRepository.findByBoardType(requestDto.getBoard_type(), pageRequest)).willReturn(pageResult);

        // when
        Page<Board> bestBoards = boardService.getBestBoards(requestDto);

        // then
        assertThat(bestBoards.getContent()).containsExactly(board);
        then(boardRepository).should(times(1)).findByBoardType(any(), eq(pageRequest));
    }

    @Test
    void testGetAllBoards() {
        // given
        BoardListRequestDto requestDto = new BoardListRequestDto();
        requestDto.setBoard_type(BoardType.ENTIRE);
        requestDto.setSearch_word("Test");
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> pageResult = new PageImpl<>(List.of(board));

        given(boardRepository.findByBoardTypeAndTitleContaining(any(), any(), eq(pageRequest))).willReturn(pageResult);

        // when
        Page<Board> boards = boardService.getAllBoards(requestDto);

        // then
        assertThat(boards.getContent()).containsExactly(board);
        then(boardRepository).should(times(1)).findByBoardTypeAndTitleContaining(any(), any(), eq(pageRequest));
    }

    @Test
    void testGetBoard() {
        // given
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // when
        Board foundBoard = boardService.getBoard(board.getId());

        // then
        assertThat(foundBoard).isEqualTo(board);
        then(boardRepository).should(times(1)).findById(anyLong());
    }


    @Test
    void testGetBoard_NotFound() {
        // given
        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> boardService.getBoard(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.BOARD_NOT_FOUND_ERROR.getMessage());

        then(boardRepository).should(times(1)).findById(anyLong());
    }

    @Test
    void testCreateBoard() throws MalformedURLException {
        // given
        MultipartFile file = mock(MultipartFile.class);
        URL mockUrl = new URL("http://example.com/image.jpg");
        List<MultipartFile> images = List.of(file, file); // 이미지 파일 2개

        // BoardRequestDto의 content에 이미지 태그 2개 설정
        String initialContent = "<p>Test content</p><img src=? /><img src=? />";
        boardRequestDto.setContent(initialContent);

        given(fileService.upload(anyString(), any())).willReturn(mockUrl);
        given(boardRepository.save(any(Board.class))).willAnswer(invocation -> {
            Board savedBoard = invocation.getArgument(0);
            return savedBoard;
        });

        // when
        Board createdBoard = boardService.createBoard(boardRequestDto, member, images);

        // then
        assertThat(createdBoard.getTitle()).isEqualTo(boardRequestDto.getTitle());
        assertThat(createdBoard.getContent()).contains("<img src='http://example.com/image.jpg' />");

        then(fileService).should(times(images.size())).upload(anyString(), any());
        then(boardRepository).should(times(1)).save(any(Board.class));
    }

    @Test
    void testUpdateBoard_NotOwner() {
        // given
        Member anotherMember = new Member(new MemberServiceDto("another@example.com", "anotherNickname"));
        Board anotherBoard = new Board(boardRequestDto, anotherMember);
        Long boardId = 1L;

        given(boardRepository.findById(boardId)).willReturn(Optional.of(anotherBoard));

        // when / then
        assertThatThrownBy(() -> boardService.updateBoard(boardId, boardRequestDto, member, Collections.emptyList()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.BOARD_DONT_HAS_OWNERSHIP_ERROR.getMessage());

        then(boardRepository).should(times(1)).findById(boardId);
        then(boardRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void testDeleteBoard() {
        // given
        Long boardId = 1L;

        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));

        // when
        boardService.deleteBoard(member, boardId);

        // then
        then(boardRepository).should(times(1)).findById(boardId);
        then(boardRepository).should(times(1)).deleteById(boardId);
    }

    @Test
    void testDeleteBoard_NotOwner() {
        // given
        Member anotherMember = new Member(new MemberServiceDto("another@example.com", "anotherNickname"));
        Board anotherBoard = new Board(boardRequestDto, anotherMember);
        Long boardId = 1L;

        given(boardRepository.findById(boardId)).willReturn(Optional.of(anotherBoard));

        // when / then
        assertThatThrownBy(() -> boardService.deleteBoard(member, boardId))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.BOARD_DONT_HAS_OWNERSHIP_ERROR.getMessage());

        then(boardRepository).should(times(1)).findById(boardId);
        then(boardRepository).shouldHaveNoMoreInteractions();
    }

}
