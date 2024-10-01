package boomerang.domain.member.service;

import boomerang.domain.kakao.domain.KakaoMember;
import boomerang.domain.member.domain.Email;
import boomerang.domain.member.domain.Member;
import boomerang.domain.member.dto.MemberServiceDto;
import boomerang.domain.member.exception.MemberNotFoundException;
import boomerang.domain.member.repository.MemberRepository;
import boomerang.global.utils.JwtUtil;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<Member> getAllMemberDomains() {
        return memberRepository.findAll();
    }

    public Member getMemberDomainById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }


    public Member getMemberDomainByEmail(Email memberEmail) {
        return memberRepository.findByEmail(memberEmail)
            .orElseThrow(MemberNotFoundException::new);
    }

    public Member createMemberDomain(MemberServiceDto memberCreateServiceDto) {
        return memberRepository.save(memberCreateServiceDto.toMemberDomain());
    }

    public String loginKakaoMember(KakaoMember kakaoMember) {
        String emailString = "kakao" + kakaoMember.id().toString(); //kakao 문자열 뒤에 id값을 붙여 kakao384747484와 같은 형식의 이메일 생성
        Email email = new Email(emailString); // String 타입의 이메일을 Email 클래스로 생성
        System.out.println("email = " + email);
        MemberServiceDto memberCreateServiceDto = new MemberServiceDto(email);

        Member member = memberRepository.findByEmail(email)
            .orElseGet(() -> memberRepository.save(memberCreateServiceDto.toMemberDomain()));
        return jwtUtil.generateToken(member.getId(),emailString);
    }

    public Member updateMemberDomain(MemberServiceDto memberCreateServiceDto) {
        validateMemberDomainExists(memberCreateServiceDto.getId());
        return memberRepository.save(memberCreateServiceDto.toMemberDomain());
    }

    public void deleteMemberDomain(Long id) {
        validateMemberDomainExists(id);
        memberRepository.deleteById(id);
    }

    private void validateMemberDomainExists(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new MemberNotFoundException();
        }
    }

}
