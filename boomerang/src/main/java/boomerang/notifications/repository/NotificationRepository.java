package boomerang.notifications.repository;

import boomerang.member.domain.Member;
import boomerang.notifications.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMemberAndIsReadFalse(Member member);

    List<Notification> findTop10ByMemberOrderByIdDesc(Member member);

}
