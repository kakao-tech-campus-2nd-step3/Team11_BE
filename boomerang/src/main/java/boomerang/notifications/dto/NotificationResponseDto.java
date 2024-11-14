package boomerang.notifications.dto;

import boomerang.notifications.domain.Notification;
import boomerang.notifications.domain.NotificationType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationResponseDto {
    private Long id;
    private String message;
    private NotificationType type;
    private String memberNickname;
    private Boolean isRead;

    public NotificationResponseDto(Notification notification) {
        this.id = notification.getId();
        this.message = notification.getMessageContent();
        this.type = notification.getNotificationType();
        this.memberNickname = notification.getMember().getNickname();
        this.isRead = notification.getIsRead();
    }
}