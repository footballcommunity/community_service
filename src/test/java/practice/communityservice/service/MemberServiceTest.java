package practice.communityservice.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import practice.communityservice.domain.exceptions.UnauthorizedException;
import practice.communityservice.domain.model.User;
import practice.communityservice.domain.model.enums.Role;
import practice.communityservice.domain.model.enums.UserStatus;
import practice.communityservice.dto.request.SigninRequestDto;
import practice.communityservice.dto.request.UpdateTokenRequestDto;
import practice.communityservice.dto.response.SigninResponseDto;
import practice.communityservice.repository.MemberRepository;
import practice.communityservice.utils.JwtUtils;
import practice.communityservice.utils.RedisUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@Slf4j
class MemberServiceTest {
    private MemberService memberService;
    private JwtUtils jwtUtils;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    RedisUtils redisUtils;

    @BeforeEach
    void setUp(){
        jwtUtils = new JwtUtils("asdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasd", 60000, 60000);
        memberService = new MemberService(
                memberRepository,
                jwtUtils,
                redisUtils
        );
    }

    @Test
    public void save(){

    }

    @Test
    @DisplayName("updateToken 성공")
    public void updateTokenSuccess(){
        // given
        User user = new User(
                1L,
                Role.MEMBER,
                "abc@naver.com",
                "username",
                "password",
                LocalDateTime.now(),
                UserStatus.ACTIVE
        );

        // when
        String accessToken = jwtUtils.createAccessToken(user.getId(), user.getEmail(), user.getRole(), user.getStatus());
        String refreshToken = jwtUtils.createRefreshToken();
        //redis에 refreshToken 저장
        Mockito.when(redisUtils.getData(user.getEmail())).thenReturn(refreshToken);
        Mockito.when(memberRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user)); // 가짜 객체 응답 정의

        // then
        SigninResponseDto signinResponseDto = memberService.updateToken(new UpdateTokenRequestDto(accessToken, refreshToken));
        Assertions.assertInstanceOf(String.class,signinResponseDto.getAccessToken());
        Assertions.assertInstanceOf(String.class, signinResponseDto.getRefreshToken());
        log.info("AccessToken : {}",signinResponseDto.getAccessToken());
        log.info("RefreshToken : {}",signinResponseDto.getRefreshToken());
    }

    @Test
    @DisplayName("updateToken 실패 토큰 불일치")
    public void updateTokenFailByUnmatchedToken(){
        // given
        User user = new User(
                1L,
                Role.MEMBER,
                "abc@naver.com",
                "username",
                "password",
                LocalDateTime.now(),
                UserStatus.ACTIVE
        );

        // when
        String accessToken = jwtUtils.createAccessToken(user.getId(), user.getEmail(), user.getRole(), user.getStatus());
        String refreshToken = jwtUtils.createRefreshToken();
        //불일치 토큰
        String unmatchedRefreshToken = jwtUtils.createRefreshToken();
        //redis에 refreshToken 저장
        Mockito.when(redisUtils.getData(user.getEmail())).thenReturn(refreshToken);
        Mockito.when(memberRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user)); // 가짜 객체 응답 정의

        // then
        Assertions.assertThrows(UnauthorizedException.class, ()->{
            SigninResponseDto signinResponseDto = memberService.updateToken(new UpdateTokenRequestDto(accessToken, unmatchedRefreshToken));
        ;},"유효하지 않은 토큰입니다");
    }

}