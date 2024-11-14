package boomerang.mentor.repository;

import boomerang.member.domain.Member;
import boomerang.mentor.domain.Mentor;
import java.util.Optional;

import boomerang.mentor.domain.MentorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    Page<Mentor> findAllByIsDeletedFalse(Pageable pageable);
    Page<Mentor> findAllByIsDeletedFalseAndMentorTypeNot(MentorType mentorType, Pageable pageable);
    Page<Mentor> findAllByIsDeletedFalseAndMentorType(MentorType mentorType, Pageable pageable);

    Optional<Mentor> findByIdAndIsDeletedFalse(Long id);

    Optional<Mentor> findByMemberAndIsDeletedFalse(Member member);

    Optional<Mentor> findByMember(Member member);
}
