package boomerang.comment.service;

import boomerang.board.domain.Board;
import boomerang.board.domain.BoardType;
import boomerang.board.dto.BoardRequestDto;
import boomerang.board.service.BoardService;
import boomerang.comment.domain.Comment;
import boomerang.comment.dto.CommentListRequestDto;
import boomerang.comment.dto.CommentRequestDto;
import boomerang.comment.repository.CommentRepository;
import boomerang.comment.util.CommentFilter;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.dto.MemberServiceDto;
import boomerang.member.service.MemberService;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private BoardService boardService;

    @Mock
    private CommentFilter commentFilter;

    @InjectMocks
    private CommentService commentService;

    private Member author;
    private Board board;
    private CommentRequestDto commentRequestDto;
    private Comment comment;

    @BeforeEach
    void setUp() {
        author = new Member(new MemberServiceDto("user@example.com", "nickname"));
        BoardRequestDto boardRequestDto = new BoardRequestDto("Sample Title", "Sample Content", BoardType.ENTIRE, null);
        board = new Board(1L, boardRequestDto, author);
        commentRequestDto = new CommentRequestDto("This is a test comment.");
        comment = new Comment(author, board, commentRequestDto);
    }

    @Test
    void testCreateComment() {
        // given
        String email = author.getEmail();
        Long boardId = board.getId();
        given(boardService.getBoard(boardId)).willReturn(board);
        given(memberService.getMemberByEmail(email)).willReturn(author);
        given(commentFilter.filterAndReplaceProfanity(commentRequestDto.getText())).willReturn(commentRequestDto.getText());
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // when
        Comment createdComment = commentService.createComment(email, boardId, commentRequestDto);

        // then
        assertThat(createdComment)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(comment);
        then(commentRepository).should(times(1)).save(any(Comment.class));
    }

    @Test
    void testDeleteComment_NotAuthor() {
        // given
        String email = "anotheruser@example.com";
        Member anotherUser = new Member(new MemberServiceDto(email, "anotherNickname"));
        Long commentId = comment.getId();

        given(commentRepository.findActiveById(commentId)).willReturn(Optional.of(comment));
        given(memberService.getMemberByEmail(email)).willReturn(anotherUser);

        // when / then
        assertThatThrownBy(() -> commentService.deleteComment(email, commentId))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.COMMENT_FORBIDDEN.getMessage());

        then(commentRepository).should(times(1)).findActiveById(commentId);
    }

    @Test
    void testUpdateComment() {
        // given
        String newText = "Updated comment text";
        String email = author.getEmail();
        Long commentId = comment.getId();
        CommentRequestDto updatedCommentRequestDto = new CommentRequestDto(newText);

        given(commentRepository.findActiveById(commentId)).willReturn(Optional.of(comment));
        given(memberService.getMemberByEmail(email)).willReturn(author);
        given(commentFilter.filterAndReplaceProfanity(newText)).willReturn(newText);
        given(commentRepository.save(any(Comment.class))).willReturn(comment); // 반환값 설정

        // when
        Comment updatedComment = commentService.updateComment(email, commentId, updatedCommentRequestDto);

        // then
        assertThat(updatedComment.getText()).isEqualTo(newText);
        then(commentRepository).should(times(1)).save(any(Comment.class));
    }


    @Test
    void testGetAllComments() {
        // given
        Long boardId = board.getId();
        CommentListRequestDto requestDto = new CommentListRequestDto(0, 10, Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> comments = new PageImpl<>(List.of(comment));

        given(commentRepository.findAllByBoardId(pageRequest, boardId)).willReturn(comments);

        // when
        Page<Comment> result = commentService.getAllComment(boardId, requestDto);

        // then
        assertThat(result.getContent())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(comment);
        then(commentRepository).should(times(1)).findAllByBoardId(pageRequest, boardId);
    }
}
