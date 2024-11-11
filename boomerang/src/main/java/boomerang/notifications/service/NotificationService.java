package boomerang.notifications.service;

import boomerang.notifications.domain.Notification;
import boomerang.notifications.dto.NotificationDto;
import boomerang.notifications.handler.NotificationWebSocketHandler;
import boomerang.notifications.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationWebSocketHandler notificationWebSocketHandler;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void sendOrSaveNotification(NotificationDto notificationDto) {
        Notification notification = new Notification(notificationDto);
        sendNotificationToUser(notification.getMember().getId(), notification.getMessageContent());
        notificationRepository.save(notification);
    }


    // 특정 사용자에게만 알림 전송
    public void sendNotificationToUser(Long userId, String messageContent) {
        messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/notifications", messageContent);
    }




}
