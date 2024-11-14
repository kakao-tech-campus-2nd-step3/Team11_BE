package boomerang.notifications.dto;

import boomerang.member.domain.Member;
import boomerang.notifications.domain.NotificationType;
import lombok.Getter;

@Getter
public class NotificationDto {

    private final Member member;

    private final NotificationType notificationType;

    private final String messageContent;

    public NotificationDto(Member member, NotificationType notificationType, String messageContent) {
        this.member = member;
        this.notificationType = notificationType;
        this.messageContent = messageContent;
    }


}
