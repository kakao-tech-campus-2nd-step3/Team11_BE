package boomerang.member.service;

import boomerang.kakao.domain.KakaoMember;
import boomerang.member.domain.Email;
import boomerang.member.domain.Member;
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

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }


    public Member getMemberByEmail(Email memberEmail) {
        return memberRepository.findByEmail(memberEmail)
            .orElseThrow(MemberNotFoundException::new);
    }

    public Member createMember(MemberServiceDto memberCreateServiceDto) {
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

}
