package boomerang.chat.service;

import boomerang.chat.domain.ChatMessage;
import boomerang.chat.domain.ChatRoom;
import boomerang.chat.dto.ChatMessageListRequestDto;
import boomerang.chat.dto.ChatMessageRequestDto;
import boomerang.chat.dto.ChatRoomRequestDto;
import boomerang.chat.repository.ChatMessageRepository;
import boomerang.chat.repository.ChatRoomRepository;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoom createChatRoom(ChatRoomRequestDto chatRoomRequestDto, Member mentor, Member client) {
        ChatRoom chatRoom = new ChatRoom(mentor, client);
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> getAllChatRooms(Member member) {
        return chatRoomRepository.findByMember(member);
    }

    public Page<ChatMessage> getChatMessages(Long chatRoomId, ChatMessageListRequestDto chatMessageListRequestDto) {
        ChatRoom chatRoom = validateChatRoomExists(chatRoomId);
        return chatMessageRepository.findByChatRoom(chatRoom, chatMessageListRequestDto.toPageRequest());
    }

    public ChatMessage sendChatMessage(ChatMessageRequestDto requestDto, Member sender) {
        ChatRoom chatRoom = validateChatRoomExists(requestDto.getChatRoomId());
        ChatMessage chatMessage = new ChatMessage(chatRoom, sender, requestDto.getContent());
        return chatMessageRepository.save(chatMessage);
    }

    public void deleteChatRoom(String memberEmail, Long roomId) {
        ChatRoom chatRoom = validateChatRoomExists(roomId);
        // 테스트를 위해 주석처리
//        if (!chatRoom.getCreator().getEmail().equals(memberEmail)) {
//            throw new BusinessException(ErrorCode.CHATROOM_DONT_HAS_OWNERSHIP_ERROR);
//        }
        chatRoomRepository.delete(chatRoom);
    }

    private ChatRoom validateChatRoomExists(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHATROOM_NOT_FOUND_ERROR));
    }
}
