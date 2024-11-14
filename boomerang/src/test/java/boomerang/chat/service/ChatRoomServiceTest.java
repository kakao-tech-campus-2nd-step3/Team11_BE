package boomerang.chat.service;

import boomerang.chat.domain.ChatMessage;
import boomerang.chat.domain.ChatRoom;
import boomerang.chat.dto.ChatMessageListRequestDto;
import boomerang.chat.repository.ChatMessageRepository;
import boomerang.chat.repository.ChatRoomRepository;
import boomerang.consultation.domain.Consultation;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.dto.MemberServiceDto;
import boomerang.mentor.domain.Mentor;
import boomerang.mentor.domain.MentorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @InjectMocks
    private ChatRoomService chatRoomService;

    private Member mentorMember;
    private Mentor mentor;
    private Member mentee;
    private Consultation consultation;
    private ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        mentorMember = new Member(new MemberServiceDto("mentor@example.com", "mentorNickname"));
        mentor = new Mentor(MentorType.LAWYER, "5 years experience", "Introduction", true, mentorMember, "contact@example.com");
        mentee = new Member(new MemberServiceDto("mentee@example.com", "menteeNickname"));
        consultation = new Consultation(mentee, mentor, LocalDateTime.now(), "Consultation Title", "Consultation Content");
        chatRoom = new ChatRoom(consultation);
    }

    @Test
    void testSaveChatMessage() {
        // given
        ChatMessage chatMessage = new ChatMessage(1L, "sender", "message", LocalDateTime.now());

        // when
        chatRoomService.saveChatMessage(chatMessage);

        // then
        then(chatMessageRepository).should(times(1)).save(chatMessage);
    }

    @Test
    void testDeleteChatRoom_NotOwner() {
        // given
        Long chatRoomId = chatRoom.getId();
        Member notOwner = new Member(new MemberServiceDto("notOwner@example.com", "notOwner"));

        given(chatRoomRepository.findById(chatRoomId)).willReturn(Optional.of(chatRoom));
        given(chatRoomRepository.findByIdAndMember(chatRoomId, notOwner)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> chatRoomService.deleteChatRoom(chatRoomId, notOwner))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.CHATROOM_DONT_HAS_OWNERSHIP_ERROR.getMessage());

        then(chatRoomRepository).should(times(1)).findById(chatRoomId);
        then(chatRoomRepository).should(times(1)).findByIdAndMember(chatRoomId, notOwner);
        then(chatRoomRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void testGetChatMessages_NotOwner() {
        // given
        Long chatRoomId = chatRoom.getId();
        Member notOwner = new Member(new MemberServiceDto("notOwner@example.com", "notOwner"));
        ChatMessageListRequestDto requestDto = new ChatMessageListRequestDto(0, 10);

        given(chatRoomRepository.findById(chatRoomId)).willReturn(Optional.of(chatRoom));
        given(chatRoomRepository.findByIdAndMember(chatRoomId, notOwner)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> chatRoomService.getChatMessages(chatRoomId, requestDto, notOwner))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.CHATROOM_DONT_HAS_OWNERSHIP_ERROR.getMessage());

        then(chatRoomRepository).should(times(1)).findById(chatRoomId);
        then(chatRoomRepository).should(times(1)).findByIdAndMember(chatRoomId, notOwner);
        then(chatRoomRepository).shouldHaveNoMoreInteractions();
    }
}
