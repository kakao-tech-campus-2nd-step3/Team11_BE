package boomerang.notifications.handler;

import boomerang.board.domain.Board;
import boomerang.comment.domain.Comment;
import boomerang.member.domain.Member;
import boomerang.notifications.domain.NotificationType;
import boomerang.notifications.dto.NotificationDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationUtil {

    public static NotificationDto createNotificationFromComment(Member targetMember, String boardTitle, String commentAuthorName) {
        return new NotificationDto(targetMember, NotificationType.COMMENT, createNotificationMessage(commentAuthorName,boardTitle));
    }

    private static String createNotificationMessage(String commentAuthorName, String boardTitle) {
        return commentAuthorName + "님이 " + boardTitle + "글에 댓글을 남겼습니다.";
    }
}
