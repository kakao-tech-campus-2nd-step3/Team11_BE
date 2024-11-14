package boomerang.notifications.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import boomerang.notifications.domain.Notification;
import boomerang.notifications.dto.NotificationDto;
import boomerang.notifications.dto.NotificationResponseDto;
import boomerang.notifications.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry userRegistry;
    private final NotificationRepository notificationRepository;
    private final MemberService memberService;

    @Transactional
    public void sendToSpecificUser(NotificationDto notificationDto) {
        Notification notification = notificationRepository.save(new Notification(notificationDto));
        String userEmail = notification.getMember().getEmail();

        // 사용자 연결 확인 로직 개선
        boolean isUserConnected = userRegistry.getUsers().stream()
                .filter(Objects::nonNull)
                .filter(user -> user.getName() != null)
                .anyMatch(user -> user.getName().equals(userEmail));

        if (isUserConnected) {
            sendMessage(notification);
            notification.markAsRead();
            log.info("실시간 알림 전송 성공: {}", userEmail);
        } else {
            log.info("사용자 미접속 - DB에만 저장: {}", userEmail);
        }
        notificationRepository.save(notification);
    }

    public void sendBroadcast(String message) {
        String destination = "/topic/notifications";
        messagingTemplate.convertAndSend(destination, message);
    }

    @Transactional
    public void sendNotificationsOfMember(String memberEmail) {
        Member member = memberService.getMemberByEmail(memberEmail);

        boolean isUserConnected = userRegistry.getUsers().stream()
                .filter(Objects::nonNull)
                .filter(user -> user.getName() != null)
                .anyMatch(user -> user.getName().equals(member.getEmail()));

        log.info("{} 사용자의 연결 여부: {}", member.getEmail(), isUserConnected);

        notificationRepository.findTop10ByMemberOrderByIdDesc(member)
                .reversed()  // 스트림으로 변환
                .forEach(
                        notification ->
                        {
                            System.out.println("notification.getId() = " + notification.getId());
                            sendMessage(notification);
                            notification.markAsRead();
                        });

    }

    private void sendMessage(Notification notification) {
        String userEmail = notification.getMember().getEmail();
        NotificationResponseDto notificationResponseDto = new NotificationResponseDto(notification);
        try {
            messagingTemplate.convertAndSendToUser(
                    userEmail,
                    "/queue/notifications",
                    notificationResponseDto
            );
            log.info("{} 유저에게 메시지 전송 전송: {}", userEmail, notificationResponseDto);
        } catch (Exception e) {
            log.error("메시지 전송 실패: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.NOTIFICATION_TRANSMISSION);
        }


    }

}