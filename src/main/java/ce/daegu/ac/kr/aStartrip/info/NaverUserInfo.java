package ce.daegu.ac.kr.aStartrip.info;

import ce.daegu.ac.kr.aStartrip.entity.Provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes;
    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public Provider getProvider() {
        return Provider.PROVIDER_NAVER;
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("nickname").toString();
    }
}
