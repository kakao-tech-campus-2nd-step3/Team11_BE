package boomerang.domain.kakao.domain;


public record Kakao_member(Kakao_profile kakao_profile, String password) {
    public String name() {
        return kakao_profile.properties().nickname();
    }

    public String email() {
        return kakao_profile.id().toString() + kakao_profile.properties().nickname();
    }

    public String password() {
        return password;
    }


}
