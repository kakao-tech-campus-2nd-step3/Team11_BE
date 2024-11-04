package boomerang.member.domain;

public enum MemberRole {
    INCOMPLETE_USER, //미완료 -> 닉네임등록으로 리다이렉트
    COMPLETE_USER; //완료 -> 홈으로 리다이렉트
}
