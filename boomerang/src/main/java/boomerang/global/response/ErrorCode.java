package boomerang.global.response;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // Global
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EG001", "Unexpected Error"),
    ACCESS_TOKEN_NOT_EXISTS_ERROR(HttpStatus.BAD_REQUEST, "EG002", "Access Token Not Exists Error"),
    JWT_ERROR(HttpStatus.UNAUTHORIZED, "EG003", "JWT token is not valid"),

    // Template
    TEMPLATE_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "EM001", "Template Not Found Error"),
    DUPLICATE_EMAIL_ERROR(HttpStatus.BAD_REQUEST, "EM002", "Duplicate Email Error"),
    DUPLICATE_NICKNAME_ERROR(HttpStatus.BAD_REQUEST, "EM003", "Duplicate Nickname Error"),

    // Board
    BOARD_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "EB001", "Board Not Found Error"),
    BOARD_DONT_HAS_OWNERSHIP_ERROR(HttpStatus.BAD_REQUEST, "EB002", "수정 또는 삭제 권한이 없는 게시글입니다"),

    //COMMENT_IS_NULL
    COMMENT_IS_NULL(HttpStatus.BAD_REQUEST, "CM_001", "댓글은 빈 내용일 수 없습니다."),
    COMMENT_FORBIDDEN(HttpStatus.BAD_REQUEST, "CM_002", "댓글에 수정 권한이 없습니다."),
    COMMENT_NON_EXISTENT(HttpStatus.BAD_REQUEST, "CM_003", "댓글을 찾을 수 없습니다."),


    //Member
    MEMBER_NON_EXISTENT(HttpStatus.BAD_REQUEST, "MB_001", "해당 멤버를 찾을 수 없습니다."),
    LOGIN_REQUIRED(HttpStatus.BAD_REQUEST, "MB_002", "로그인이 필요합니다."),


    //진행도
    PROGRESS_TYPE_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "PG_001", "진행도타입검사 요청 객체가 잘못되었습니다."),
    PROGRESS_TYPE_EXISTS(HttpStatus.BAD_REQUEST, "PG_002", "이미 진행도 검사를 완료했습니다."),
    PROGRESS_TYPE_NON_EXISTENT(HttpStatus.BAD_REQUEST, "PG_003", "진행도 검사를 완료하지 않았습니다."),
    PROGRESS_NON_EXISTENT(HttpStatus.BAD_REQUEST, "PG_004", "진행도가 생성되지 않았습니다. 진행도 타입 검사를 안했을 수도 있습니다."),
    PROGRESS_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "PG_005", "해당 세부 단계를 찾을 수 없습니다."),
    PROGRESS_SUB_INVALID_NAME(HttpStatus.BAD_REQUEST, "PG_006", "세부 단계의 이름이 잘못되었습니다."),
    PROGRESS_MAIN_INVALID_NAME(HttpStatus.BAD_REQUEST, "PG_007", "메인 단계의 이름이 잘못되었습니다."),
    PROGRESS_SUB_MAIN_DO_NOT_MATCH(HttpStatus.BAD_REQUEST, "PG_008", "서브단계와 메인 단계가 적절하게 매칭되지 않습니다."),
    PROGRESS_SUB_ERROR(HttpStatus.BAD_REQUEST, "PG_009", "해당 메인단계에 대한 정보가 없습니다."),
    PROGRESS_MAIN_ERROR(HttpStatus.BAD_REQUEST, "PG_010", "해당 세부단계에 대한 정보가 없습니다."),


    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return code + " : " + message;
    }
}
