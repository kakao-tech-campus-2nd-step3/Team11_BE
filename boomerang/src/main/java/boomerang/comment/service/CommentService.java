package boomerang.comment.service;

import boomerang.board.domain.Board;
import boomerang.board.service.BoardService;
import boomerang.comment.domain.Comment;
import boomerang.comment.dto.CommentListRequestDto;
import boomerang.comment.dto.CommentRequestDto;
import boomerang.comment.dto.CommentResponseDto;
import boomerang.comment.repository.CommentRepository;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final MemberService memberService;

    //댓글 생성
    public void createComment(String email, Long boardId, CommentRequestDto commentRequestDto) {
        Board board = boardService.getBoard(boardId);
        Member author = memberService.getMemberByEmail(email);

        commentRepository.save(new Comment(author, board, commentRequestDto));
    }

    //댓글 조회
    public Page<Comment> getAllComment(Long boardId, CommentListRequestDto commentListRequestDto) {

        PageRequest pageRequest = getPageRequest(commentListRequestDto);
        Page<Comment> commentPage = commentRepository.findAllByBoardId(pageRequest, boardId);

        //페이지 만들어서 제공
        return commentPage;
    }


    //댓글 삭제 (논리)
    public void deleteComment(String email, Long commentId) {
        Comment comment = getComment(commentId);

        if (comment.isMemberCommentAuthor(memberService.getMemberByEmail(email))) {
            throw new BusinessException(ErrorCode.COMMENT_FORBIDDEN);
        }

        //논리삭제
        comment.softDelete();

        commentRepository.save(comment);
    }


    //댓글 수정
    public void updateComment(String email, Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = getComment(commentId);

        if (comment.isMemberCommentAuthor(memberService.getMemberByEmail(email))) {
            throw new BusinessException(ErrorCode.COMMENT_FORBIDDEN);
        }

        comment.updateCommentText(commentRequestDto.getText());

        commentRepository.save(comment);
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NON_EXISTENT));
    }


    private CommentResponseDto createCommentResponseDto(Comment comment, boolean isUserLoggedIn, Member loginMember) {
        if (!isUserLoggedIn) {
            return new CommentResponseDto(comment); // 로그인하지 않은 경우
        }
        return new CommentResponseDto(comment, comment.isMemberCommentAuthor(loginMember)); // 로그인한 경우
    }

    private PageRequest getPageRequest(CommentListRequestDto commentListRequestDto) {
        return PageRequest.of(
                commentListRequestDto.getPage(),
                commentListRequestDto.getSize(),
                Sort.by(commentListRequestDto.getSortDirection(), commentListRequestDto.getSortBy())
        );
    }

}
