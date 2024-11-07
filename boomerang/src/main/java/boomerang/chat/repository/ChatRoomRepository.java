package boomerang.chat.repository;

import boomerang.chat.domain.ChatRoom;
import boomerang.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT c FROM ChatRoom c WHERE c.mentor = :member OR c.client = :member")
    List<ChatRoom> findByMember(@Param("member") Member member);
}
