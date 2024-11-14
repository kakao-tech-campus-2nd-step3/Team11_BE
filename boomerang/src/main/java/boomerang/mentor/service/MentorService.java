package boomerang.mentor.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.global.response.PageResponseDto;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import boomerang.mentor.domain.Mentor;
import boomerang.mentor.domain.MentorType;
import boomerang.mentor.dto.*;
import boomerang.mentor.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public MentorInitialListResponseDto getInitialMentorPage(MentorInitialListRequestDto mentorInitialListRequestDto) {
        // 추천 전문가
        PageRequest pageRequest =
                PageRequest.of(0, mentorInitialListRequestDto.getRecommended_size(),
                        Sort.by(Sort.Direction.DESC, "score"));
        Page<Mentor> recommendedMentorPage =
                mentorRepository.findAllByIsDeletedFalse(pageRequest);
        Page<MentorResponseDto> recommendedMentorResponseDtoPage = recommendedMentorPage.map(MentorResponseDto::new);

        // 답변 많은 전문가
        pageRequest =
                PageRequest.of(0, mentorInitialListRequestDto.getRecommended_size(),
                        Sort.by(Sort.Direction.DESC, "replyCount"));
        Page<Mentor> expertMentorPage =
                mentorRepository.findAllByIsDeletedFalseAndMentorTypeNot(MentorType.PREVIOUS_DAMAGE_RESOLVER, pageRequest);
        Page<MentorResponseDto> expertMentorResponseDtoPage = expertMentorPage.map(MentorResponseDto::new);

        // 답변 많은 일반인
        pageRequest =
                PageRequest.of(0, mentorInitialListRequestDto.getRecommended_size(),
                        Sort.by(Sort.Direction.DESC, "replyCount"));
        Page<Mentor> normalMentorPage =
                mentorRepository.findAllByIsDeletedFalseAndMentorType(MentorType.PREVIOUS_DAMAGE_RESOLVER, pageRequest);
        Page<MentorResponseDto> normalMentorResponseDtoPage = normalMentorPage.map(MentorResponseDto::new);

        // 일반 멘토 전체 조회
        pageRequest =
                PageRequest.of(0, mentorInitialListRequestDto.getRecommended_size(),
                        Sort.by(Sort.Direction.DESC, "id"));
        Page<Mentor> mentorPage = mentorRepository.findAllByIsDeletedFalse(pageRequest);
        Page<MentorResponseDto> mentorResponseDtoPage = mentorPage.map(MentorResponseDto::new);

        return new MentorInitialListResponseDto(
                recommendedMentorResponseDtoPage, expertMentorResponseDtoPage,
                normalMentorResponseDtoPage, mentorResponseDtoPage
        );
    }

    @Transactional(readOnly = true)
    public PageResponseDto<MentorResponseDto> getAllMentors(MentorListRequestDto mentorListRequestDto) {
        // 일반 멘토 전체 조회
        PageRequest pageRequest =
                PageRequest.of(mentorListRequestDto.getPage(), mentorListRequestDto.getSize(),
                        Sort.by(Sort.Direction.DESC, "id"));

        Page<Mentor> mentorPage = mentorRepository.findAllByIsDeletedFalse(pageRequest);
        Page<MentorResponseDto> mentorResponseDtoPage = mentorPage.map(MentorResponseDto::new);

        return new PageResponseDto<>(mentorResponseDtoPage);
    }

    @Transactional(readOnly = true)
    public MentorResponseDto getMentorProfile(Long id) {
        Mentor mentor = getMentor(id);
        return new MentorResponseDto(mentor);
    }

    @Transactional
    public MentorResponseDto createMentor(String email, MentorCreateRequestDto requestDto) {
        Member member = memberService.getMemberByEmail(email);
        member.changeMentor();

        if (!member.isEmailVerified()) {
            throw new BusinessException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        // 기존에 기록이 있으면 재생성, 없다면 신규 생성
        return mentorRepository.findByMember(member)
                .map(existingMentor -> reactivateExistingMentor(existingMentor, requestDto))
                .orElseGet(() -> createNewMentor(member, requestDto));
    }

    private MentorResponseDto reactivateExistingMentor(Mentor mentor,
                                                       MentorCreateRequestDto requestDto) {
        if (!mentor.getIsDeleted()) {
            throw new BusinessException(ErrorCode.MENTOR_ALREADY_EXISTS);
        }

        mentor.updateMentor(requestDto);

        Mentor savedMentor = mentorRepository.save(mentor);
        return new MentorResponseDto(savedMentor);
    }

    private MentorResponseDto createNewMentor(Member member, MentorCreateRequestDto requestDto) {
        Mentor newMentor = new Mentor(
                requestDto.getMentorType(),
                requestDto.getCareer(),
                requestDto.getIntroduce(),
                requestDto.getAdvertisementStatus(),
                member,
                requestDto.getContact()
        );

        Mentor savedMentor = mentorRepository.save(newMentor);
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

        mentor.updateMentor(updateRequestDto);

        return new MentorResponseDto(mentor);
    }

    @Transactional
    public void deleteMentor(String email) {
        Member member = memberService.getMemberByEmail(email);
        Mentor mentor = mentorRepository.findByMemberAndIsDeletedFalse(member)
                .orElseThrow(() -> new BusinessException(ErrorCode.MENTOR_NOT_FOUND));

        mentor.delete();
    }

    public Mentor getMentor(Long id) {
        return mentorRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MENTOR_NOT_FOUND));
    }
}
