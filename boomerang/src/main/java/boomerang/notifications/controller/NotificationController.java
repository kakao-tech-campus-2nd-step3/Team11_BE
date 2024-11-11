package boomerang.notifications.controller;

import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import boomerang.notifications.domain.NotificationType;
import boomerang.notifications.dto.NotificationDto;
import boomerang.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final MemberService memberService;

    @ResponseBody
    @PostMapping("/message")
    public void sendMessage(@RequestBody String massage) {
        Member member = memberService.getMember(15L);
        NotificationDto notificationDto = new NotificationDto(member, NotificationType.COMMENT, massage);
        notificationService.sendOrSaveNotification(notificationDto);
    }

    @GetMapping("/massage")
    public String sendMessage() {
        return "notification.html";
    }



}
