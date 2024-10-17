package boomerang.mentor.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import boomerang.mentor.domain.Mentor;
import boomerang.mentor.dto.MentorCreateRequestDto;
import boomerang.mentor.dto.MentorResponseDto;
import boomerang.mentor.dto.MentorUpdateRequestDto;
import boomerang.mentor.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public Page<MentorResponseDto> getAllMentors(Pageable pageable) {
        Page<Mentor> mentorPage = mentorRepository.findAllByIsDeletedFalse(pageable);
        return mentorPage.map(MentorResponseDto::new);
    }

    @Transactional(readOnly = true)
    public MentorResponseDto getMentorById(Long id) {
        Mentor mentor = mentorRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.MENTOR_NOT_FOUND));
        return new MentorResponseDto(mentor);
    }

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

    @Transactional
    public MentorResponseDto updateMentor(String email, MentorUpdateRequestDto updateRequestDto) {
        Member member = memberService.getMemberByEmail(email);
        Mentor mentor = mentorRepository.findByMemberAndIsDeletedFalse(member)
            .orElseThrow(() -> new BusinessException(ErrorCode.MENTOR_NOT_FOUND));

        if (!mentor.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.MENTOR_UPDATE_NOT_AUTHORIZED);
        }

        mentor.updateMentor(
            updateRequestDto.getMentorType(),
            updateRequestDto.getCareer(),
            updateRequestDto.getIntroduce(),
            updateRequestDto.getAdvertisementStatus(),
            updateRequestDto.getContact()
        );

        return new MentorResponseDto(mentor);
    }

    @Transactional
    public void deleteMentor(String email) {
        Member member = memberService.getMemberByEmail(email);
        Mentor mentor = mentorRepository.findByMemberAndIsDeletedFalse(member)
            .orElseThrow(() -> new BusinessException(ErrorCode.MENTOR_NOT_FOUND));

        mentor.delete();
    }
}
