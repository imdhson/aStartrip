package ce.daegu.ac.kr.aStartrip.dto;

import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.info.OAuth2UserInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Slf4j
@Data
public class MemberDetails implements UserDetails, OAuth2User {

    private final Member member;
    private OAuth2UserInfo oAuth2UserInfo;

    public MemberDetails(Member member) {
        this.member = member;
    }

    //OAuth2==================================
    public MemberDetails(Member member, OAuth2UserInfo oAuth2UserInfo) {
        this.member = member;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2UserInfo.getAttributes();
    }

    @Override
    public String getName() {
        return oAuth2UserInfo.getProviderId();
    }
    //=======================================
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectors = new ArrayList<>();
        return collectors;
    } // 일단은 권한 설정을 보류

    @Override
    public String getPassword() {
        log.info(member.getPW());
        return member.getPW();
    }

    @Override
    public String getUsername() {
        log.info(member.getEmail());
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    // 계정이 만료되지 않았는지 리턴 (true : 만료안됨.)

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    // 계정이 잠겨있는지 않았는지 리턴 (true : 잠기지 않음.)

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    // 비밀번호가 만료되지 않았는지 리턴 (true : 만료 안됨.)

    @Override
    public boolean isEnabled() {// 계정이 활성화인지 리턴.
        return member.isActivation();
    }
}
