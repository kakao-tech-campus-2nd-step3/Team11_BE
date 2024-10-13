package boomerang.progress.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.repository.MemberRepository;
import boomerang.progress.domain.*;
import boomerang.progress.dto.*;
import boomerang.progress.repository.ProgressARepository;
import boomerang.progress.repository.ProgressBRepository;
import boomerang.progress.repository.ProgressCRepository;
import boomerang.progress.repository.ProgressDRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgressService {
    private final MemberRepository memberRepository;
    private final ProgressARepository progressARepository;
    private final ProgressBRepository progressBRepository;
    private final ProgressCRepository progressCRepository;
    private final ProgressDRepository progressDRepository;


    //유저의 타입 체크 & 진행도 테이블을 언제 만들지?
    public ProgressType checkUserType(PrincipalDetails principalDetails, ProgressTypeRequestDto progressTypeRequestDto) {
        Member member = getMemberOrThrow(principalDetails.getMemberEmail());
        if (member.hasProgressType()) {
            throw new BusinessException(ErrorCode.PROGRESS_TYPE_EXISTS);
        }

        ProgressType progressType = checkType(progressTypeRequestDto);

        //멤버 엔티티에 피해 유형 등록
        member.registerProgressType(progressType);

        createAndSaveProgress(progressType, member);

        memberRepository.save(member);
        return progressType;
    }


    //유저의 타입 조회
    public ProgressType getUserType(PrincipalDetails principalDetails) {
        Member member = getMemberOrThrow(principalDetails.getMemberEmail());

        return Optional.ofNullable(member.getProgressType())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_TYPE_NON_EXISTENT));
    }

    //유저의 진행도 조회
    public ProgressResponseDto getUserProgress(PrincipalDetails principalDetails) {
        Member member = getMemberOrThrow(principalDetails.getMemberEmail());

        ProgressType memberProgressType = Optional.ofNullable(member.getProgressType())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_TYPE_NON_EXISTENT));

        if (memberProgressType.equals(ProgressType.A)) {
            return new ProgressAResponseDto(findProgressA(member));
        }
        if (memberProgressType.equals(ProgressType.B)) {
            return new ProgressBResponseDto(findProgressB(member));
        }
        if (memberProgressType.equals(ProgressType.C)) {
            return new ProgressCResponseDto(findProgressC(member));
        }
        return new ProgressDResponseDto(findProgressD(member));

    }

    private ProgressA findProgressA(Member member) {
        return progressARepository.findByMemberId(member.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_TYPE_NON_EXISTENT));
    }

    private ProgressB findProgressB(Member member) {
        return progressBRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_TYPE_NON_EXISTENT));
    }

    private ProgressC findProgressC(Member member) {
        return progressCRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_TYPE_NON_EXISTENT));
    }

    private ProgressD findProgressD(Member member) {
        return progressDRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_TYPE_NON_EXISTENT));
    }


    private Member getMemberOrThrow(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NON_EXISTENT));
    }

    private ProgressType checkType(ProgressTypeRequestDto progressTypeRequestDto) {
        Boolean isMemberInsureds = progressTypeRequestDto.getIsInsured();
        Boolean isMemberContractTerminated = progressTypeRequestDto.getIsContractTerminated();
        if (isMemberInsureds && isMemberContractTerminated) {
            return ProgressType.D;
        }
        if (!isMemberInsureds && isMemberContractTerminated) {
            return ProgressType.C;
        }
        if (isMemberInsureds && !isMemberContractTerminated) {
            return ProgressType.B;
        }
        if (!isMemberInsureds && !isMemberContractTerminated) {
            return ProgressType.A;
        }
        throw new BusinessException(ErrorCode.PROGRESS_TYPE_REQUEST_ERROR);
    }



    private void createAndSaveProgress(ProgressType progressType, Member member) {
        if (progressType.equals(ProgressType.A)) {
            progressARepository.save(new ProgressA(member));
        }
        if (progressType.equals(ProgressType.B)) {
            progressBRepository.save(new ProgressB(member));
        }
        if (progressType.equals(ProgressType.C)) {
            progressCRepository.save(new ProgressC(member));
        }
        if (progressType.equals(ProgressType.D)) {
            progressDRepository.save(new ProgressD(member));
        }
    }

}
