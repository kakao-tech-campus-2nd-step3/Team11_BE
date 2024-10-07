package boomerang.global.handler;

import boomerang.global.response.ErrorCode;
import boomerang.global.response.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper 생성

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");


        // ErrorResponseDto를 JSON으로 변환
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.LOGIN_REQUIRED);
        String jsonResponse = objectMapper.writeValueAsString(errorResponseDto);

        response.getWriter().write(jsonResponse);    }
}
