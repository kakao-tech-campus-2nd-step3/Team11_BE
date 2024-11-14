package boomerang.chat.controller;

import boomerang.chat.domain.ChatMessage;
import boomerang.chat.dto.ChatMessageListRequestDto;
import boomerang.chat.dto.ChatMessageResponseDto;
import boomerang.chat.service.ChatRoomService;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.response.PageResponseDto;
import boomerang.global.utils.ResponseHelper;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// 채팅방은 상담이 Ongoing 으로 바뀔 때, 자동으로 생성된다
// ConsultationStatusScheduler 참고
@Slf4j
@Controller
@RequestMapping("/api/v1/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    public ChatRoomController(ChatRoomService chatRoomService, MemberService memberService) {
        this.chatRoomService = chatRoomService;
        this.memberService = memberService;
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long roomId
    ) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());

        chatRoomService.deleteChatRoom(roomId, member);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<PageResponseDto<ChatMessageResponseDto>> getChatMessages(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long roomId,
            @ModelAttribute ChatMessageListRequestDto chatMessageListRequestDto
    ) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Page<ChatMessage> chatMessagePage = chatRoomService.getChatMessages(roomId, chatMessageListRequestDto, member);
        Page<ChatMessageResponseDto> chatMessageResponseDtoPage = chatMessagePage.map(ChatMessageResponseDto::new);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new PageResponseDto<>(chatMessageResponseDtoPage));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException e) {
        log.error(e.toString());
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }
}
