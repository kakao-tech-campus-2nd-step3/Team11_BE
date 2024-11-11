package boomerang.chat.repository;

import boomerang.chat.domain.ChatRoom;
import boomerang.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // Pageable을 사용하여 Page로 반환
    @Query("SELECT c FROM ChatRoom c WHERE c.mentor = :member OR c.mentee = :member")
    Page<ChatRoom> findByMember(@Param("member") Member member, Pageable pageable);

    @Query("SELECT c FROM ChatRoom c WHERE c.id = :chatRoomId AND (c.mentor = :member OR c.mentee = :member)")
    Optional<ChatRoom> findByIdAndMember(@Param("chatRoomId") Long chatRoomId, @Param("member") Member member);

}
