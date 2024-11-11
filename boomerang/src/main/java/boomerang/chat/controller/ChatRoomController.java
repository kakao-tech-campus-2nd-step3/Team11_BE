package boomerang.chat.controller;

import boomerang.chat.domain.ChatMessage;
import boomerang.chat.domain.ChatRoom;
import boomerang.chat.dto.*;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("")
    public ResponseEntity<PageResponseDto<ChatRoomResponseDto>> getAllChatRooms(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @ModelAttribute ChatRoomListRequestDto chatRoomListRequestDto
    ) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Page<ChatRoom> chatRoomPage = chatRoomService.getAllChatRooms(chatRoomListRequestDto, member);
        Page<ChatRoomResponseDto> chatRoomResponseDtoPage = chatRoomPage.map(ChatRoomResponseDto::new);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new PageResponseDto<>(chatRoomResponseDtoPage));
    }

    @PostMapping("")
    public ResponseEntity<Void> createChatRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ChatRoomRequestDto chatRoomRequestDto
    ) {
        Member mentee = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Member mentor = memberService.getMemberByEmail(chatRoomRequestDto.getMentorEmail());

        chatRoomService.createChatRoom(chatRoomRequestDto, mentor, mentee);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> createChatRoom(
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
