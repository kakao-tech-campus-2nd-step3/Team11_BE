package boomerang.consultation.service;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.dto.ConsultationRequestDto;
import boomerang.consultation.dto.ConsultationResponseDto;
import boomerang.consultation.repository.ConsultationRepository;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import boomerang.mentor.domain.Mentor;
import boomerang.mentor.domain.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final MemberService memberService;

    public ConsultationResponseDto requestConsultation(PrincipalDetails principalDetails, ConsultationRequestDto consultationRequestDto) {
        Member mentee = memberService.getMemberByEmail(principalDetails.getMemberEmail());
//        Mentor mentor = mentorRepository.findById(1L).orElseThrow();
        LocalDate localDate = LocalDate.now();

        Consultation savedConsultation = consultationRepository.save(new Consultation(mentee, mentor, localDate));

        return new ConsultationResponseDto(savedConsultation);
    }

    public ConsultationResponseDto completeConsultation(PrincipalDetails principalDetails, Long consultationId) {
        Member mentee = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Consultation consultation = getConsultation(consultationId);

        if (!consultation.isMentee(mentee)) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTEE);
        }

        consultation.complete();

        Consultation savedConsultation = consultationRepository.save(consultation);
        return new ConsultationResponseDto(savedConsultation);
    }

//    public ConsultationResponseDto getConsultationOfMentor (PrincipalDetails principalDetails,) {
//        Member mentee = memberService.getMemberByEmail(principalDetails.ge tMemberEmail());
//        Consultation consultation = getConsultation(consultationId);
//
//        if (!consultation.isMentee(mentee)) {
//            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTEE);
//        }
//
//        consultation.complete();
//
//        Consultation savedConsultation = consultationRepository.save(consultation);
//        return new ConsultationResponseDto(savedConsultation);
//    }



    public Consultation getConsultation(long id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CONSULTATION_NOT_FOUND_ERROR));
    }

}
