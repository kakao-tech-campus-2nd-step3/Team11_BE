//package boomerang.global.response;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Objects;
//
//@Getter
//@Setter
//public class ResultResponseDto<T> {
//    private String code;
//    private String message;
//    private T data;
//
//    // 기본 생성자
//    public ResultResponseDto(ResultCode resultCode, T data) {
//        this.code = resultCode.getCode();
//        this.message = resultCode.getMessage();
//        this.data = data;
//    }
//
//    public ResultResponseDto(String code, String message, T data) {
//        this.code = code;
//        this.message = message;
//        this.data = data;
//    }
//
//    @Override
//    public String toString() {
//        return "ResultResponseDto{" +
//                "code='" + code + '\'' +
//                ", message='" + message + '\'' +
//                ", data=" + data +
//                '}';
//    }
//
//    public static ResponseEntity<?> createResponse(ResultCode resultCode, Objects objects){
//        return new ResponseEntity<>(objects, resultCode.getStatus());
//    }
//}
