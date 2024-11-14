package boomerang.notifications.domain;

import boomerang.member.domain.Member;
import boomerang.notifications.dto.NotificationDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 설정
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private NotificationType notificationType;

    private String messageContent;

    private Boolean isRead;


    public Notification(NotificationDto notificationDto) {
        this.member = notificationDto.getMember();
        this.notificationType = notificationDto.getNotificationType();
        this.messageContent = notificationDto.getMessageContent();
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }


}
