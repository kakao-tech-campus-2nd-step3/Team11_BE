package boomerang.global.oauth.service;

import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.member.domain.Member;
import boomerang.member.exception.MemberNotFoundException;
import boomerang.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    // 필수 메서드 구현 (UserDetailsService 인터페이스의 메서드)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username을 email로 간주하여 이메일 기반으로 유저 로드
        return loadUserByEmail(username);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(MemberNotFoundException::new);
        return new PrincipalDetails(member);
    }    // 필수 메서드 구현 (UserDetailsService 인터페이스의 메서드)

    public Boolean existUserByUsername(String email) {
        // username을 email로 간주하여 이메일 기반으로 유저 로드
        return memberRepository.existsByEmail(email);
    }

}
