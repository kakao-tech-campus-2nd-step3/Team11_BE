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

    // Like
    DUPLICATE_LIKE_ERROR(HttpStatus.BAD_REQUEST, "EL001", "Duplicate Like Error"),
    NOT_MEMBERS_LIKE_ERROR(HttpStatus.FORBIDDEN, "EL002", "Not Members Like Error"),

    BOARD_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "EB001", "Board Not Found Error"),
    BOARD_DONT_HAS_OWNERSHIP_ERROR(HttpStatus.BAD_REQUEST, "EB002", "수정 또는 삭제 권한이 없는 게시글입니다"),

    //COMMENT_IS_NULL
    COMMENT_IS_NULL(HttpStatus.BAD_REQUEST,"CM_001","댓글은 빈 내용일 수 없습니다."),
    COMMENT_FORBIDDEN(HttpStatus.BAD_REQUEST,"CM_002" ,"댓글에 수정 권한이 없습니다." ),
    COMMENT_NON_EXISTENT(HttpStatus.BAD_REQUEST,"CM_003" ,"댓글을 찾을 수 없습니다." ),



    //Member
    MEMBER_NON_EXISTENT(HttpStatus.BAD_REQUEST,"MB_001" ,"해당 멤버를 찾을 수 없습니다." ),
    LOGIN_REQUIRED(HttpStatus.BAD_REQUEST,"MB_002" ,"로그인이 필요합니다." ),


    //Like
    LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT,"LK_001" ,"이미 좋아요한 게시물입니다." ),



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
