package boomerang.chat.controller;

import boomerang.chat.domain.ChatRoom;
import boomerang.chat.dto.ChatRoomRequestDto;
import boomerang.chat.service.ChatRoomService;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> getAllChatRooms(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRooms(member);
        return ResponseEntity.status(HttpStatus.OK).body(chatRooms);
    }

    @PostMapping("/room")
    public ResponseEntity<Void> createChatRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ChatRoomRequestDto chatRoomRequestDto
    ) {
        Member mentee = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Member mentor = memberService.getMemberByEmail(chatRoomRequestDto.getMentorEmail());

        chatRoomService.createChatRoom(chatRoomRequestDto, mentor, mentee);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/rooms/page")
    public String getChatRoomsPage(Model model) {
        return "chat_rooms";
    }

    @GetMapping("/room/{roomId}")
    public String getChatRoomPage(@PathVariable Long roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "chat_room";
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException e) {
        log.error(e.toString());
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }
}
