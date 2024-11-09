package boomerang.chat.repository;

import boomerang.chat.domain.ChatMessage;
import boomerang.chat.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findByChatRoom(ChatRoom chatRoom, Pageable pageable);
}
