package boomerang.chat.controller;

import boomerang.chat.domain.ChatRoom;
import boomerang.chat.dto.ChatRoomRequestDto;
import boomerang.chat.service.ChatRoomService;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 목록 조회
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> getAllChatRooms() {
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRooms();
        return ResponseEntity.status(HttpStatus.OK)
                .body(chatRooms);
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<Void> createChatRoom(
            @Valid @RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        chatRoomService.createChatRoom(chatRoomRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // WebSocket 연결을 위한 엔드포인트 설명
    @GetMapping("/ws")
    public String websocketInfo() {
        return "WebSocket endpoint: /ws/chat/{room_id}";
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException e) {
        log.error(e.toString());
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }
}
