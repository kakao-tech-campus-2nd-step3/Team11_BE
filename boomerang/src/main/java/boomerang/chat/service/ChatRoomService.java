package boomerang.chat.service;

import boomerang.chat.domain.ChatMessage;
import boomerang.chat.domain.ChatRoom;
import boomerang.chat.dto.ChatMessageListRequestDto;
import boomerang.chat.dto.ChatRoomListRequestDto;
import boomerang.chat.repository.ChatMessageRepository;
import boomerang.chat.repository.ChatRoomRepository;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public Page<ChatRoom> getAllChatRooms(ChatRoomListRequestDto chatRoomListRequestDto, Member member) {
        PageRequest pageRequest = getChatRoomPageRequest(chatRoomListRequestDto);

        return chatRoomRepository.findByMember(member, pageRequest);
    }

    private PageRequest getChatRoomPageRequest(ChatRoomListRequestDto chatRoomListRequestDto) {
        return PageRequest.of(
                chatRoomListRequestDto.getPage(),
                chatRoomListRequestDto.getSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );
    }

    public Page<ChatMessage> getChatMessages(Long chatRoomId, ChatMessageListRequestDto chatMessageListRequestDto, Member member) {
        validateChatRoomOwnership(chatRoomId, member);
        PageRequest pageRequest = getMessagePageRequest(chatMessageListRequestDto);

        return chatMessageRepository.findByChatRoomId(chatRoomId, pageRequest);
    }

    private static PageRequest getMessagePageRequest(ChatMessageListRequestDto chatMessageListRequestDto) {
        return PageRequest.of(
                chatMessageListRequestDto.getPage(),
                chatMessageListRequestDto.getSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );
    }

    @Async
    public void saveChatMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }

    public void deleteChatRoom(Long chatRoomId, Member member) {
        ChatRoom chatRoom = validateChatRoomOwnership(chatRoomId, member);

        chatRoomRepository.delete(chatRoom);
    }

    private ChatRoom validateChatRoomExists(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHATROOM_NOT_FOUND_ERROR));
    }

    // 채팅방 소유자 검증
    public ChatRoom validateChatRoomOwnership(Long chatRoomId, Member sender) {
        validateChatRoomExists(chatRoomId);

        return chatRoomRepository.findByIdAndMember(chatRoomId, sender)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHATROOM_DONT_HAS_OWNERSHIP_ERROR));
    }
}
