package boomerang.notifications.service;

import boomerang.notifications.domain.Notification;
import boomerang.notifications.dto.NotificationDto;
import boomerang.notifications.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry userRegistry;
    private final NotificationRepository notificationRepository;

    public NotificationService(
            @Lazy SimpMessagingTemplate messagingTemplate,
            @Lazy SimpUserRegistry userRegistry,
            NotificationRepository notificationRepository) {
        this.messagingTemplate = messagingTemplate;
        this.userRegistry = userRegistry;
        this.notificationRepository = notificationRepository;
    }


    @Transactional
    public void sendToSpecificUser(NotificationDto notificationDto) {
        Notification notification = new Notification(notificationDto);
        String userEmail = notification.getMember().getEmail();

        // 사용자 연결 확인 로직 개선
        boolean isUserConnected = userRegistry.getUsers().stream()
                .filter(Objects::nonNull)
                .filter(user -> user.getName() != null)
                .anyMatch(user -> user.getName().equals(userEmail));

        try {
            if (isUserConnected) {
                messagingTemplate.convertAndSendToUser(
                        userEmail,
                        "/queue/notifications",
                        notification
                );
                notification.markAsRead();
                log.info("실시간 알림 전송 성공: {}", userEmail);
            } else {
                log.info("사용자 미접속 - DB에만 저장: {}", userEmail);
            }
        } catch (Exception e) {
            log.error("메시지 전송 실패: {}", e.getMessage(), e);
        }

        notificationRepository.save(notification);
    }

    public void sendBroadcast(String message) {
        String destination = "/topic/notifications";
        messagingTemplate.convertAndSend(destination, message);
    }

}