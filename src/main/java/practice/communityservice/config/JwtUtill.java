package practice.communityservice.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import practice.communityservice.domain.exceptions.ErrorCode;
import practice.communityservice.domain.exceptions.UnauthorizedException;
import practice.communityservice.domain.model.enums.Role;
import practice.communityservice.domain.model.enums.UserStatus;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@Component
public class JwtUtill {

    private final Key key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;

    public JwtUtill(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access_token_expiration_time}") long accessTokenExpTime,
            @Value("${jwt.refresh_token_expiration_time}") long refreshTokenExpTime){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    // AccessToken 생성
    public String createAccessToken(String userEmail, Role role, UserStatus status) {
        Claims claims = Jwts.claims();
        claims.put("userEmail", userEmail);
        claims.put("role", role);
        claims.put("status", status);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + this.accessTokenExpTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // RefreshToken 생성
    public String createRefreshToken(){
        Date now = new Date();
        return Jwts.builder()
                .setExpiration(new Date(now.getTime() + this.refreshTokenExpTime))
                .signWith(this.key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String resolveToken(HttpServletRequest request){
        return request.getHeader("Authorization");
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | IllegalArgumentException | MalformedJwtException e ) {
            throw new UnauthorizedException(ErrorCode.INVALID_JWT, "유효하지 않은 토큰입니다.");
        } catch (ExpiredJwtException e){
            throw new UnauthorizedException(ErrorCode.INVALID_JWT, "만료된 토큰입니다.");
        } catch (UnsupportedJwtException e){
            throw new UnauthorizedException(ErrorCode.INVALID_JWT, "지원하지 않은 토큰입니다.");
        } catch (Exception e){
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        String id = Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token).getBody().getSubject();
        return new UsernamePasswordAuthenticationToken(id, null, new ArrayList<>());
    }
}
