package boomerang.chat.controller;

import boomerang.chat.domain.ChatRoom;
import boomerang.chat.dto.ChatRoomRequestDto;
import boomerang.chat.service.ChatRoomService;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> getAllChatRooms() {
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRooms();
        return ResponseEntity.status(HttpStatus.OK).body(chatRooms);
    }

    @PostMapping("/room")
    public ResponseEntity<Void> createChatRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        chatRoomService.createChatRoom(chatRoomRequestDto);
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
