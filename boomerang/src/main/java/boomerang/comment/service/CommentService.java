package boomerang.comment.service;

import boomerang.board.domain.Board;
import boomerang.board.service.BoardService;
import boomerang.comment.domain.Comment;
import boomerang.comment.dto.CommentListRequestDto;
import boomerang.comment.dto.CommentRequestDto;
import boomerang.comment.repository.CommentRepository;
import boomerang.comment.util.CommentFilter;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import boomerang.notifications.handler.NotificationUtil;
import boomerang.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentFilter commentFilter;
    private final NotificationService notificationService;

    public Comment createComment(String email, Long boardId, CommentRequestDto commentRequestDto) {
        Board board = boardService.getBoard(boardId);
        board.increaseCommentCount();
        Member author = memberService.getMemberByEmail(email);

        String filteredText = validateCommentText(commentRequestDto.getText());

        CommentRequestDto filteredCommentRequestDto = new CommentRequestDto(filteredText);

        Comment comment = commentRepository.save(new Comment(author, board, filteredCommentRequestDto));

        Member targetMember = comment.getBoard().getMember();
        String boardTitle = comment.getBoard().getTitle();
        String commentAuthorName = comment.getAuthorName();

        log.info("댓글 정보: {} {} {}", targetMember, boardTitle, commentAuthorName);
        notificationService.sendToSpecificUser(NotificationUtil.createNotificationFromComment(targetMember,boardTitle,commentAuthorName));
        return comment;
    }

    public Page<Comment> getAllComment(Long boardId, CommentListRequestDto commentListRequestDto) {

        PageRequest pageRequest = getPageRequest(commentListRequestDto);
        Page<Comment> commentPage = commentRepository.findAllByBoardId(pageRequest, boardId);

        return commentPage;
    }


    public void deleteComment(String email, Long commentId) {
        Comment comment = getComment(commentId);

        if (!comment.isMemberCommentAuthor(memberService.getMemberByEmail(email))) {
            throw new BusinessException(ErrorCode.COMMENT_FORBIDDEN);
        }

        comment.getBoard().decreaseCommentCount();
        comment.softDelete();

        commentRepository.save(comment);
    }


    public Comment updateComment(String email, Long commentId,
        CommentRequestDto commentRequestDto) {
        Comment comment = getComment(commentId);

        if (!comment.isMemberCommentAuthor(memberService.getMemberByEmail(email))) {
            throw new BusinessException(ErrorCode.COMMENT_FORBIDDEN);
        }

        String filteredText = validateCommentText(commentRequestDto.getText());
        comment.updateCommentText(filteredText);

        return commentRepository.save(comment);
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findActiveById(commentId)
            .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NON_EXISTENT));
    }

    private PageRequest getPageRequest(CommentListRequestDto commentListRequestDto) {
        return PageRequest.of(
            commentListRequestDto.getPage(),
            commentListRequestDto.getSize(),
            Sort.by(commentListRequestDto.getSortDirection(), commentListRequestDto.getSortBy())
        );
    }

    private String validateCommentText(String text) {
        if (commentFilter.containsPhoneNumber(text)) {
            throw new BusinessException(ErrorCode.COMMENT_CONTAINS_PHONE_NUMBER);
        }

        return commentFilter.filterAndReplaceProfanity(text);
    }

}
