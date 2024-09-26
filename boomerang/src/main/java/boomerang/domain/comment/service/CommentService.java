package boomerang.domain.comment.service;

import boomerang.domain.board.domain.Board;
import boomerang.domain.board.repository.BoardRepository;
import boomerang.domain.comment.domain.Comment;
import boomerang.domain.comment.dto.CommentRequestDto;
import boomerang.domain.comment.repository.CommentRepository;
import boomerang.domain.member.domain.Member;
import boomerang.domain.member.repository.MemberRepository;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    private Member getMemberOrThrow(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NON_EXISTENT));
    }


    private Board getBoardOrThrow(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NON_EXISTENT));
    }

}
