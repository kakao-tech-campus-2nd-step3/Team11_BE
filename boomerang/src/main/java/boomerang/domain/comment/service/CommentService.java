package boomerang.domain.comment.service;

import boomerang.domain.board.domain.Board;
import boomerang.domain.board.repository.BoardRepository;
import boomerang.domain.comment.domain.Comment;
import boomerang.domain.comment.dto.CommentRequestDto;
import boomerang.domain.comment.dto.CommentResponseDto;
import boomerang.domain.comment.repository.CommentRepository;
import boomerang.domain.member.domain.Member;
import boomerang.domain.member.repository.MemberRepository;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public void createComment(String email, Long boardId, CommentRequestDto commentRequestDto) {
        Member author = getMemberOrThrow(email);
        Board board = getBoardOrThrow(boardId);

        commentRepository.save(Comment.builder().
                commentText(commentRequestDto.getCommentText())
                .author(author)
                .board(board)
                .build());
    }

    public Page<CommentResponseDto> getAllComment(String email, Long boardId, Pageable pageable) {
        Member loginUser = getMemberOrThrow(email);
        Board board = getBoardOrThrow(boardId);
        Page<Comment> comments = commentRepository.findAllByBoardIdAndIsDeletedNot(pageable, board.getId());


        //댓글응답객체페이지 본문 만들기
        List<CommentResponseDto> commentResponseContent = comments.getContent()
                .stream()
                .map(comment -> CommentResponseDto.of(comment,isUserCommentAuthor(loginUser,comment)))
                .toList();

        //페이지 만들어서 제공
        return new PageImpl<>(commentResponseContent, pageable, comments.getTotalElements());

    }


    public void deleteComment(String email, Long commentId) {
        Member loginUser = getMemberOrThrow(email);
        Comment comment = getCommentOrThrow(commentId);

        if (isUserCommentAuthor(loginUser, comment)) {
            throw new BusinessException(ErrorCode.COMMENT_FORBIDDEN);
        }

        //논리삭제
        comment.softDelete();

        commentRepository.save(comment);
    }

    private Member getMemberOrThrow(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NON_EXISTENT));
    }

    private Board getBoardOrThrow(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NON_EXISTENT));
    }

    private Comment getCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NON_EXISTENT));
    }

    public boolean isUserCommentAuthor(Member loginUser, Comment comment) {
        return !comment.getAuthor().equals(loginUser);
    }
}
