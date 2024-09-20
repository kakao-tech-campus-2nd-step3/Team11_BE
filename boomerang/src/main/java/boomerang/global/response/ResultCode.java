package boomerang.global.response;

import org.springframework.http.HttpStatus;

public enum ResultCode {
    // Auth
    LOGIN_SUCCESS(HttpStatus.OK, "A001", "로그인 성공"),

    // Product
    GET_ALL_PRODUCTS_SUCCESS(HttpStatus.OK, "P001", "모든 제품 조회 성공"),
    GET_PRODUCT_BY_ID_SUCCESS(HttpStatus.OK, "P002", "단일 제품 조회 성공"),
    CREATE_PRODUCT_SUCCESS(HttpStatus.OK, "P003", "제품 추가 성공"),
    UPDATE_PRODUCT_SUCCESS(HttpStatus.OK, "P004", "제품 수정 성공"),
    DELETE_PRODUCT_SUCCESS(HttpStatus.OK, "P005", "제품 삭제 성공"),

    // Member
    GET_ALL_MEMBERS_SUCCESS(HttpStatus.OK, "M001", "모든 회원 조회 성공"),
    GET_MEMBER_BY_ID_SUCCESS(HttpStatus.OK, "M002", "단일 회원 조회 성공"),
    CREATE_MEMBER_SUCCESS(HttpStatus.OK, "M003", "회원 가입 성공"),
    UPDATE_MEMBER_SUCCESS(HttpStatus.OK, "M004", "회원 수정 성공"),
    DELETE_MEMBER_SUCCESS(HttpStatus.OK, "M005", "회원 삭제 성공"),

    // Wishes
    GET_ALL_WISHES_SUCCESS(HttpStatus.OK, "W001", "모든 위시 리스트 조회 성공"),
    CREATE_WISH_SUCCESS(HttpStatus.OK, "W002", "위시 리스트 추가 성공"),
    UPDATE_WISH_SUCCESS(HttpStatus.OK, "W003", "위시 리스트 수정 성공"),
    DELETE_WISH_SUCCESS(HttpStatus.OK, "W004", "위시 리스트 삭제 성공"),
    ;

    // status 를 HttpStatus 로 관리하는 것이 좋을까, 아니면 int로 관리하는 것이 좋을까?
    private final HttpStatus status;
    private final String code;
    private final String message;

    ResultCode(HttpStatus status, String code, String message) {
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
}
