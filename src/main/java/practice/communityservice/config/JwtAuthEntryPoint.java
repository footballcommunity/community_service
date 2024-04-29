package practice.communityservice.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import practice.communityservice.domain.exceptions.ErrorCode;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");

        // API Access without JWT token or invalid JWT
        if (errorCode == null || errorCode == ErrorCode.INVALID_JWT) {
            this.setResponse(response, ErrorCode.INVALID_JWT, "ACCESS TOKEN 만료");
            return;
        }

        // API Access with JWT token includes user data whose role is NONE or state is not ACTIVE
        if (errorCode == ErrorCode.NEED_SIGN_IN) {
            this.setResponse(response, ErrorCode.NEED_SIGN_IN, "다시 로그인 해주세요.");
        }
    }

    private void setResponse(
            HttpServletResponse response,
            ErrorCode errorCode,
            String message
    ) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println(
                "{" +
                        "\"errorCode\" : \"" + errorCode.getCode() + "\"," +
                        "\"message\" : \"" + message + "\"," +
                        "\"timeStamp\" : \"" + LocalDateTime.now() + "\"" +
                        "}"
        );
    }
}
