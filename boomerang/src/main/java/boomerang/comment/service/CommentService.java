package boomerang.comment.service;

import boomerang.board.domain.Board;
import boomerang.board.service.BoardService;
import boomerang.comment.repository.CommentRepository;
import boomerang.comment.domain.Comment;
import boomerang.comment.dto.CommentRequestDto;
import boomerang.comment.dto.CommentResponseDto;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        commentRepository.save(new Comment(author,board,commentRequestDto));
    }

    //댓글 조회
    public Page<CommentResponseDto> getAllComment(PrincipalDetails principalDetails, Long boardId, Pageable pageable) {

        Board board = boardService.getBoard(boardId);
        Page<Comment> comments = commentRepository.findAllByBoardIdAndIsDeletedNot(pageable, board.getId());
        List<CommentResponseDto> commentResponseContent = new ArrayList<>();


        //로그인 여부
        boolean isUserLoggedIn = principalDetails != null;
        Member loginMember = isUserLoggedIn ? memberService.getMemberByEmail(principalDetails.getMemberEmail()) : null;

        //댓글 응답 객체페이지 본문 만들기
        commentResponseContent = comments.getContent()
                .stream()
                .map(comment -> createCommentResponseDto(comment,isUserLoggedIn,loginMember))
                .toList();


        //페이지 만들어서 제공
        return new PageImpl<>(commentResponseContent, pageable, comments.getTotalElements());

    }


    //댓글 삭제 (논리)
    public void deleteComment(String email, Long commentId) {
        Comment comment = getComment(commentId);

        if (comment.isMemberCommentAuthor(memberService.getMemberByEmail(email))){
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

}
