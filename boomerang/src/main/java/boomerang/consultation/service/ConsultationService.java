package boomerang.consultation.service;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.dto.ConsultationRequestDto;
import boomerang.consultation.dto.ConsultationResponseDto;
import boomerang.consultation.dto.ConsultationResponseListDto;
import boomerang.consultation.repository.ConsultationRepository;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import boomerang.mentor.domain.Mentor;
import boomerang.mentor.service.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final MemberService memberService;
    private final MentorService mentorService;

    public ConsultationResponseDto requestConsultation(PrincipalDetails principalDetails, ConsultationRequestDto consultationRequestDto) {
        Member mentee = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Mentor mentor = mentorService.getMentor(consultationRequestDto.getMentorId());
        LocalDate localDate = LocalDate.now();

        if (consultationRepository.existsByMenteeAndMentorAndConsultationDate(mentee, mentor, localDate)) {
            throw new BusinessException(ErrorCode.CONSULTATION_ALREADY_EXISTS);
        }


        Consultation savedConsultation = consultationRepository.save(new Consultation(mentee, mentor, localDate));

        return new ConsultationResponseDto(savedConsultation);
    }

    public ConsultationResponseDto completeConsultation(PrincipalDetails principalDetails, Long consultationId) {
        Member mentee = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Consultation consultation = getConsultation(consultationId);

        if (!consultation.isMentee(mentee)) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTEE);
        }

        if (consultation.isFinished()) {
            throw new BusinessException(ErrorCode.CONSULTATION_ALREADY_FINISHED);
        }

        consultation.complete();

        Consultation savedConsultation = consultationRepository.save(consultation);
        return new ConsultationResponseDto(savedConsultation);
    }

    public ConsultationResponseDto getConsultationDetail(Long consultationId) {
        Consultation consultation = getConsultation(consultationId);
        return new ConsultationResponseDto(consultation);
    }


    public ConsultationResponseListDto getConsultationOfUser(PrincipalDetails principalDetails, Pageable pageable) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());

        Page<ConsultationResponseDto> consultationResponsePage = consultationRepository.findAllByMentee(member, pageable)
                .map(ConsultationResponseDto::new);

        return new ConsultationResponseListDto(consultationResponsePage);
    }

    public ConsultationResponseListDto getConsultationOfMentor(Long mentorId, Pageable pageable) {
        Mentor mentor = mentorService.getMentor(mentorId);
        Page<ConsultationResponseDto> consultationResponsePage = consultationRepository.findAllByMentor(mentor, pageable)
                .map(ConsultationResponseDto::new);

        return new ConsultationResponseListDto(consultationResponsePage);

    }

    public Consultation getConsultation(long id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CONSULTATION_NOT_FOUND_ERROR));
    }

}
