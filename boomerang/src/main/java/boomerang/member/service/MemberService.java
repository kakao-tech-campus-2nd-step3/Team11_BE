package boomerang.member.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.global.utils.JwtUtil;
import boomerang.kakao.domain.KakaoMember;
import boomerang.member.domain.Member;
import boomerang.member.domain.RandomNickname;
import boomerang.member.dto.MemberServiceDto;
import boomerang.member.exception.MemberNotFoundException;
import boomerang.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final RandomNickname randomNicknameGenerator;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil, RandomNickname randomNicknameGenerator) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.randomNicknameGenerator = randomNicknameGenerator;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }


    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    public String createMember(MemberServiceDto memberCreateServiceDto) {
        Member member = memberRepository.save(new Member(memberCreateServiceDto));
        return jwtUtil.generateToken(member.getId(), member.getEmail());
    }

    public Member loginKakaoMember(KakaoMember kakaoMember) {
        String email = kakaoMember.email(); //카카오에서 받은 이메일을 emailString으로 저장
        return memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(kakaoMember)));
    }

//    안쓰는 로직 : 팀원들과 상의 후 삭제 예정
//    public Member updateMember(Long id,MemberCreateRequestDto memberCreateRequestDTO) {
//        Member member = getMember(id);
//        member.update(memberCreateRequestDTO);
//        return memberRepository.save(member);
//    }
//
//    public void deleteMember(Long id) {
//        validateMemberExists(id);
//        memberRepository.deleteById(id);
//    }

    private void validateMemberExists(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new MemberNotFoundException();
        }
    }

    // 중복되지 않는 유니크한 닉네임을 생성하는 메서드
    public String generateUniqueNickname() {
        // 기본 랜덤 닉네임 생성
        return randomNicknameGenerator.generateRandomNickname();
    }

    public Member updateNickname(String email, String newNickname) {
        Member member = getMemberByEmail(email);
        if (memberRepository.existsByNickname(newNickname)) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME_ERROR);
        }

        member.updateNickname(newNickname);
        memberRepository.save(member);

        return member;
    }

}
