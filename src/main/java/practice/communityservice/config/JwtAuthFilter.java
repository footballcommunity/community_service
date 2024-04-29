package practice.communityservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtill jwtUtill;
    // JwtUtill을 활용해 Jwt 인증 수행
    // 인증된 정보는 SecurityContextHolder에 저장
    // 요청 당 한번만 수행 (redirect)
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.jwtUtill.resolveToken((HttpServletRequest) request);
        if(token != null && this.jwtUtill.validateToken(token)){
            Authentication auth = this.jwtUtill.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        log.debug("JwtFilter={}",token);
        // Invoke next filter
        filterChain.doFilter(request, response);
    }
}
