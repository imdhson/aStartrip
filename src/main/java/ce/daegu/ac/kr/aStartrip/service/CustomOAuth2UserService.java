package ce.daegu.ac.kr.aStartrip.service;

import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import ce.daegu.ac.kr.aStartrip.entity.Member;
import ce.daegu.ac.kr.aStartrip.info.KakaoUserInfo;
import ce.daegu.ac.kr.aStartrip.info.NaverUserInfo;
import ce.daegu.ac.kr.aStartrip.info.OAuth2UserInfo;
import ce.daegu.ac.kr.aStartrip.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;
        //provider 이름 조회
        String provider = userRequest.getClientRegistration().getRegistrationId();

        //추가 확장
        if(provider.equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        }else if(provider.equals("kakao")) {
            // oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }

        String email = oAuth2UserInfo.getEmail();
        Optional<Member> e = memberRepository.findById(email);
        Member m = null;
        if(!e.isPresent()){
            m = Member.builder()
                    .email(email).name(oAuth2UserInfo.getName())
                    .activation(true).provider(oAuth2UserInfo.getProvider()).build();
            memberRepository.save(m);
        }else {
            m = e.get();
        }
        return new MemberDetails(m, oAuth2UserInfo);
    }
}
