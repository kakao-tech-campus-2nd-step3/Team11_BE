package boomerang.mentor.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import boomerang.mentor.domain.Mentor;
import boomerang.mentor.dto.MentorCreateRequestDto;
import boomerang.mentor.dto.MentorResponseDto;
import boomerang.mentor.repository.MentorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;
    private final MemberService memberService;

    @Transactional
    public MentorResponseDto createMentor(String email, MentorCreateRequestDto requestDto) {
        Member member = memberService.getMemberByEmail(email);

        if (mentorRepository.existsByMember(member)) {
            throw new BusinessException(ErrorCode.MENTOR_ALREADY_EXISTS);
        }

        Mentor mentor = new Mentor(
            requestDto.getMentorType(),
            requestDto.getCareer(),
            requestDto.getIntroduce(),
            requestDto.getAdvertisementStatus(),
            member,
            requestDto.getContact()
        );

        Mentor savedMentor = mentorRepository.save(mentor);
        return new MentorResponseDto(savedMentor);
    }
}
