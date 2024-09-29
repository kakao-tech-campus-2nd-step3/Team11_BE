package boomerang.global.oauth.dto;

import boomerang.domain.member.domain.Email;
import boomerang.domain.member.domain.MemberDomain;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class PrincipalDetails implements UserDetails {
    private MemberDomain memberDomain;

    public void setMemberDomain(MemberDomain memberDomain) {
        this.memberDomain = memberDomain;
    }

    public PrincipalDetails(MemberDomain memberDomain) {
        this.memberDomain = memberDomain;
    }
    @Override
    public String getPassword() {
        return "password";
    }

    @Override
    public String getUsername() {
        return "";
    }

//    @Override
    public Email getMemberEmail() {
        return memberDomain.getEmail();
    }

    //계정이 살아있는지 리턴 : ture -> 만료 X / false -> 만료됨
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠기지 않았는지 리턴 : true -> 잠기지 않음
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호가 만료되지 않았는지 리턴 : ture -> 만료 X
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화되어있는지 리턴 : true -> 만료 X
    @Override
    public boolean isEnabled() {
        return true;
    }

    //계정의 권한을 리턴...!!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //타입이 특이하군용..?!
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(()-> "ROLE_"+memberDomain.getMemberType());
        return collection;
    }
}