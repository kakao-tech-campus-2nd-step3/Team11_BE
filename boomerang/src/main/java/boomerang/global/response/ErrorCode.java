package boomerang.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Global
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EG001", "Unexpected Error"),
    ACCESS_TOKEN_NOT_EXISTS_ERROR(HttpStatus.BAD_REQUEST, "EG002", "Access Token Not Exists Error"),
    JWT_ERROR(HttpStatus.UNAUTHORIZED, "EG003", "JWT token is not valid"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "EG004", "잘못된 요청입니다."),
    COOKIES_ERROR(HttpStatus.UNAUTHORIZED, "EG005", "닉네임 쿠키 생성중 오류가 발생했습니다."),
    KAKAO_ERROR(HttpStatus.UNAUTHORIZED, "EG006", "카카오 로그인 중 에러가 발생했습니다."),
    FILE_ERROR(HttpStatus.UNAUTHORIZED, "EG007", "욕설 파일 읽어오는 과정 중 에러가 발생했습니다."),

    // Template
    TEMPLATE_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "EM001", "Template Not Found Error"),
    DUPLICATE_EMAIL_ERROR(HttpStatus.BAD_REQUEST, "EM002", "Duplicate Email Error"),
    DUPLICATE_NICKNAME_ERROR(HttpStatus.BAD_REQUEST, "EM003", "Duplicate Nickname Error"),

    // Like
    DUPLICATE_LIKE_ERROR(HttpStatus.BAD_REQUEST, "LK001", "Duplicate Like Error"),
    NOT_MEMBERS_LIKE_ERROR(HttpStatus.FORBIDDEN, "LK002", "Not Members Like Error"),
    LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, "LK003", "이미 좋아요한 게시물입니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "LK004", "해당 좋아요를 찾을 수 없습니다."),

    // Board
    BOARD_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "EB001", "Board Not Found Error"),
    BOARD_DONT_HAS_OWNERSHIP_ERROR(HttpStatus.BAD_REQUEST, "EB002", "수정 또는 삭제 권한이 없는 게시글입니다"),
    IMAGE_COUNT_MISMATCH_ERROR(HttpStatus.BAD_REQUEST, "EB003", "이미지 파일 수가 콘텐츠의 이미지 태그 수와 일치하지 않습니다"),

    // Comment
    COMMENT_IS_NULL(HttpStatus.BAD_REQUEST, "CM001", "댓글은 빈 내용일 수 없습니다."),
    COMMENT_FORBIDDEN(HttpStatus.FORBIDDEN, "CM002", "댓글에 수정 권한이 없습니다."),
    COMMENT_NON_EXISTENT(HttpStatus.NOT_FOUND, "CM003", "댓글을 찾을 수 없습니다."),
    COMMENT_CONTAINS_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "CM004", "전화번호를 올릴 수 없습니다."),

    // Member
    MEMBER_NON_EXISTENT(HttpStatus.BAD_REQUEST, "MB001", "해당 멤버를 찾을 수 없습니다."),
    LOGIN_REQUIRED(HttpStatus.BAD_REQUEST, "MB002", "로그인이 필요합니다."),

    // Mentor
    MENTOR_ALREADY_EXISTS(HttpStatus.CONFLICT, "MT_001", "이미 멘토로 등록된 사용자입니다."),
    MENTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "MT_002", "해당 멘토를 찾을 수 없습니다."),
    MENTOR_UPDATE_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "MT003", "멘토 정보 수정 권한이 없습니다."),
    MENTOR_NOT_REGISTERED(HttpStatus.CONFLICT, "MT_001", "멘토로 등록되지 않은 사용자입니다."),

    //Consultation
    CONSULTATION_NOT_A_MENTEE(HttpStatus.UNAUTHORIZED, "CS001", "로그인한 멤버가 상담의 멘티가 아닙니다."),
    CONSULTATION_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "CS002", "해당 상담은 존재하지 않습니다."),
    CONSULTATION_ALREADY_EXISTS(HttpStatus.NOT_FOUND, "CS003", "같은날짜에 동일한 상담이 존재합니다."),
    CONSULTATION_ALREADY_FINISHED(HttpStatus.NOT_FOUND, "CS004", "해당 상담은 이미 완료되었습니다."),
    CONSULTATION_TIME_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "CS005", "등록 가능 시간은 0시에서 23시 사이여야 합니다."),
    CONSULTATION_NOT_A_MENTOR(HttpStatus.UNAUTHORIZED, "CS006", "로그인한 멤버가 상담의 멘토가 아닙니다."),
    SCHEDULE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "CS007", "해당 일정은 존재하지 않습니다."),
    CONSULTATION_ALREADY_CONFIRMED(HttpStatus.NOT_FOUND,"CS008","해당 상담은 이미 확정되었습니다."),
    CONSULTATION_NOT_CHANGED(HttpStatus.BAD_REQUEST, "CS009", "유효하지 않는 ENUM값 입니다."),
    // Progress
    PROGRESS_TYPE_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "PG001", "진행도 타입검사 요청 객체가 잘못되었습니다."),
    PROGRESS_TYPE_EXISTS(HttpStatus.CONFLICT, "PG002", "이미 진행도 검사를 완료했습니다."),
    PROGRESS_TYPE_NON_EXISTENT(HttpStatus.BAD_REQUEST, "PG003",
        "유저의 타입정보가 없습니다. 진행도 검사를 완료하지 않았습니다."),
    PROGRESS_NON_EXISTENT(HttpStatus.NOT_FOUND, "PG004",
        "진행도가 생성되지 않았습니다. 진행도 타입 검사를 안했을 수도 있습니다."),
    PROGRESS_REQUEST_ERROR(HttpStatus.NOT_FOUND, "PG005", "해당 세부 단계를 찾을 수 없습니다."),
    PROGRESS_SUB_INVALID_NAME(HttpStatus.BAD_REQUEST, "PG006", "세부 단계의 이름이 잘못되었습니다."),
    PROGRESS_MAIN_INVALID_NAME(HttpStatus.BAD_REQUEST, "PG007", "메인 단계의 이름이 잘못되었습니다."),
    PROGRESS_SUB_MAIN_DO_NOT_MATCH(HttpStatus.BAD_REQUEST, "PG008", "서브단계와 메인 단계가 적절하게 매칭되지 않습니다."),
    PROGRESS_NOT_INCLUDED_SUB(HttpStatus.NOT_FOUND, "PG009", "유저의 피해타입은 해당 세부단계를 가지고 있지 않습니다."),
    PROGRESS_NOT_INCLUDED_MAIN(HttpStatus.NOT_FOUND, "PG010", "유저의 피해타입은 해당 메인단계를 가지고 있지 않습니다."),
    PROGRESS_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "PG011", "해당 서브단계는 이미 완료 단계입니다"),
    PROGRESS_ALREADY_INCOMPLETE(HttpStatus.BAD_REQUEST, "PG012", "해당 서브단계는 이미 미완료 단계입니다."),
    PROGRESS_REQUEST_MAIN_STEP_IS_NOT_THE_CURRENT_STEP(HttpStatus.BAD_REQUEST, "PG013", "해당 서브단계는 유저의 현재 단계가 아닙니다."),


    // Chat
    CHATROOM_DONT_HAS_OWNERSHIP_ERROR(HttpStatus.FORBIDDEN, "CH001", "채팅방에 대한 소유 권한이 없습니다."),
    CHATROOM_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "CH002", "해당 채팅방을 찾을 수 없습니다."),

    // File
    S3_UPLOAD_ERROR(HttpStatus.NOT_FOUND, "EF001", "S3와 정상적인 연결이 불가능합니다"),

    // Mail
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "EM001", "인증 코드가 만료되었습니다"),
    VERIFICATION_CODE_INVALID(HttpStatus.BAD_REQUEST, "EM002", "잘못된 인증 코드입니다"),
    EMAIL_NOT_VERIFIED(HttpStatus.FORBIDDEN, "EM003", "이메일 인증이 필요합니다"),

    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return code + " : " + message;
    }
}
