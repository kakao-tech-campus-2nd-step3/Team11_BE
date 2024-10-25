package boomerang.chat.service;

import boomerang.chat.domain.ChatRoom;
import boomerang.chat.domain.ChatMessage;
import boomerang.chat.dto.ChatRoomRequestDto;
import boomerang.chat.dto.ChatMessageListRequestDto;
import boomerang.chat.dto.ChatMessageRequestDto;
import boomerang.chat.repository.ChatRoomRepository;
import boomerang.chat.repository.ChatMessageRepository;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅방 생성
    public ChatRoom createChatRoom(ChatRoomRequestDto requestDto) {
        ChatRoom chatRoom = new ChatRoom(requestDto.getName());
        return chatRoomRepository.save(chatRoom);
    }

    // 모든 채팅방 조회
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    // 특정 채팅방 메시지 조회
    public Page<ChatMessage> getChatMessages(Long chatRoomId, ChatMessageListRequestDto chatMessageListRequestDto) {
        ChatRoom chatRoom = validateChatRoomExists(chatRoomId);
        return chatMessageRepository.findByChatRoom(chatRoom, chatMessageListRequestDto.toPageRequest());
    }

    // 메시지 전송
    public ChatMessage sendChatMessage(ChatMessageRequestDto requestDto, Member sender) {
        ChatRoom chatRoom = validateChatRoomExists(requestDto.getChatRoomId());
        ChatMessage chatMessage = new ChatMessage(chatRoom, sender, requestDto.getContent());
        return chatMessageRepository.save(chatMessage);
    }

    // 채팅방 삭제
    public void deleteChatRoom(String memberEmail, Long roomId) {
        ChatRoom chatRoom = validateChatRoomExists(roomId);
        if (!chatRoom.getCreator().getEmail().equals(memberEmail)) {
            throw new BusinessException(ErrorCode.CHATROOM_DONT_HAS_OWNERSHIP_ERROR);
        }
        chatRoomRepository.delete(chatRoom);
    }

    // 채팅방 존재 여부 확인
    private ChatRoom validateChatRoomExists(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHATROOM_NOT_FOUND_ERROR));
    }
}
