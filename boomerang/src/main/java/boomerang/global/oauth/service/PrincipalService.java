package boomerang.global.oauth.service;

import boomerang.domain.member.domain.Email;
import boomerang.domain.member.domain.MemberDomain;
import boomerang.domain.member.exception.MemberNotFoundException;
import boomerang.domain.member.repository.MemberRepository;
import boomerang.global.oauth.dto.PrincipalDetails;
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
        MemberDomain memberDomain = memberRepository.findByEmail(new Email(email))
            .orElseThrow(MemberNotFoundException::new);
        return new PrincipalDetails(memberDomain);
    }    // 필수 메서드 구현 (UserDetailsService 인터페이스의 메서드)

    public Boolean existUserByUsername(String email) {
        // username을 email로 간주하여 이메일 기반으로 유저 로드
        return memberRepository.existsByEmail(new Email(email));
    }

}
