package ce.daegu.ac.kr.aStartrip.info;

import ce.daegu.ac.kr.aStartrip.entity.Provider;

import javax.management.relation.Role;
import java.util.Map;

public interface OAuth2UserInfo {
    Map<String, Object> getAttributes();
    String getProviderId();
    Provider getProvider();
    String getEmail();
    String getName();
}
