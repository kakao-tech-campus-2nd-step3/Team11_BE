package boomerang.member.service;

import boomerang.kakao.domain.KakaoMember;
import boomerang.member.domain.Member;
import boomerang.member.domain.RandonNickname;
import boomerang.member.dto.MemberServiceDto;
import boomerang.member.exception.MemberNotFoundException;
import boomerang.member.repository.MemberRepository;
import boomerang.global.utils.JwtUtil;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final RandonNickname randomNicknameGenerator;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil, RandonNickname randomNicknameGenerator) {
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
        Member member = memberRepository.save(memberCreateServiceDto.toMemberDomain());
        return jwtUtil.generateToken(member.getId(),member.getEmail());
    }

    public String loginKakaoMember(KakaoMember kakaoMember) {
        String email= kakaoMember.email(); //카카오에서 받은 이메일을 emailString으로 저장
        String nickname = generateUniqueNickname();// 카카오 닉네임이 아닌 랜덤 닉네임 생성기로부터 닉네임 받기
        System.out.println("email = " + email);
        MemberServiceDto memberCreateServiceDto = new MemberServiceDto(email, nickname);

        Member member = memberRepository.findByEmail(email)
            .orElseGet(() -> memberRepository.save(memberCreateServiceDto.toMemberDomain()));
        return jwtUtil.generateToken(member.getId(),member.getEmail());
    }

    public Member updateMember(MemberServiceDto memberCreateServiceDto) {
        validateMemberExists(memberCreateServiceDto.getId());
        return memberRepository.save(memberCreateServiceDto.toMemberDomain());
    }

    public void deleteMember(Long id) {
        validateMemberExists(id);
        memberRepository.deleteById(id);
    }

    private void validateMemberExists(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new MemberNotFoundException();
        }
    }

    // 중복되지 않는 유니크한 닉네임을 생성하는 메서드
    public String generateUniqueNickname() {
        // 기본 랜덤 닉네임 생성
        String baseNickname = randomNicknameGenerator.generateRandomNickname();

//        // DB에서 같은 패턴의 닉네임 중 가장 큰 숫자 찾기
//        Integer maxSuffix = memberRepository.findMaxSuffixByNicknamePattern(baseNickname);

//        // 중복된 닉네임이 없으면 그대로 사용, 있으면 큰 숫자에 1을 더해 닉네임 생성
//        String uniqueNickname = (maxSuffix == null) ? baseNickname : baseNickname + (maxSuffix + 1);

        return baseNickname;
    }

}
