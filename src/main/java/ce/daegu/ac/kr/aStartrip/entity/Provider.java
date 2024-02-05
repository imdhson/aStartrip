package ce.daegu.ac.kr.aStartrip.entity;

public enum Provider {
    PROVIDER_LOCAL("PROVIDER_LOCAL"),
    PROVIDER_NAVER("PROVIDER_NAVER"),
    PROVIDER_KAKAO("PROVIDER_KAKAO");

    private String provider;

    Provider(String provider){
        this.provider = provider;
    }

    public String value(){
        return provider;
    }
}
