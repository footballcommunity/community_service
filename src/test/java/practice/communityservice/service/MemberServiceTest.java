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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import practice.communityservice.domain.exceptions.BadRequestException;
import practice.communityservice.domain.exceptions.ErrorCode;
import practice.communityservice.domain.exceptions.UnauthorizedException;
import practice.communityservice.domain.model.User;
import practice.communityservice.domain.model.UserDetails;
import practice.communityservice.domain.model.enums.Role;
import practice.communityservice.domain.model.enums.UserStatus;
import practice.communityservice.dto.request.SigninRequestDto;
import practice.communityservice.dto.request.SignoutRequestDto;
import practice.communityservice.dto.request.SignupRequestDto;
import practice.communityservice.dto.request.UpdateTokenRequestDto;
import practice.communityservice.dto.response.SigninResponseDto;
import practice.communityservice.dto.response.SignupResponseDto;
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
    private PasswordEncoder passwordEncoder;
    @MockBean
    private SecurityContextHolder securityContextHolder;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    RedisUtils redisUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils("asdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasd", 60000, 60000);
        passwordEncoder = new BCryptPasswordEncoder();

        memberService = new MemberService(
                memberRepository,
                jwtUtils,
                redisUtils,
                passwordEncoder
        );
    }

    // 회원가입 성공
    @Test
    @DisplayName("회원가입 성공")
    void signUpSuccess() {
        // Given
        SignupRequestDto signupRequestDto = new SignupRequestDto(Role.MEMBER, "email@naver.com", "username", "pwd", UserStatus.ACTIVE);
        SignupRequestDto.builder()
                .email("email@naver.com")
                .username("username")
                .password("pwd")
                .status(UserStatus.ACTIVE)
                .role(Role.MEMBER)
                .build();
        // When
        // Then
        assertDoesNotThrow(() -> memberService.signup(signupRequestDto));

    }

    // 회원가입 이메일 중복
    @Test
    @DisplayName("회원가입 이메일 중복")
    void signUpFailByDuplicatedEmail() {
        // Given
        // 이미 존재하는 이메일
        String email = "email@email.com";
        // 이미 존재하는 유저
        User firstUser = User.builder()
                .id(1L)
                .email(email)
                .status(UserStatus.ACTIVE)
                .password("pwd")
                .dateCreated(LocalDateTime.now())
                .role(Role.MEMBER)
                .build();

        SignupRequestDto signupRequestDto = new SignupRequestDto(Role.MEMBER, email, "newusername", "password", UserStatus.ACTIVE);
        // When
        Mockito.when(memberRepository.findByEmail(email)).thenReturn(Optional.of(firstUser)); // 기존 유저 존재

        // Then
        assertThrows(BadRequestException.class, () -> memberService.signup(signupRequestDto));
    }

    // 로그인 성공
    @Test
    @DisplayName("로그인 성공")
    void signInSuccess() {
        // Given
        String email = "email@email.com";
        String pwd = "pwd";
        String endcodedPwd = passwordEncoder.encode(pwd);
        User findUser = User.builder()
                .id(1L)
                .email(email)
                .password(endcodedPwd)
                .dateCreated(LocalDateTime.now())
                .role(Role.MEMBER)
                .status(UserStatus.ACTIVE)
                .build();
        SigninRequestDto signinRequestDto = new SigninRequestDto(email, pwd);

        // When
        Mockito.when(memberRepository.findByEmail(email)).thenReturn(Optional.of(findUser)); // 로그인 DB 조회

        // Then
        assertDoesNotThrow(() -> memberService.signin(signinRequestDto));
    }

    @Test
    @DisplayName("로그인 실패 비밀번호 다름")
    void signInFailByPwd() {

        // Given
        String email = "email@email.com";
        String pwd = "pwd";
        String endcodedPwd = passwordEncoder.encode(pwd);
        User findUser = User.builder()
                .id(1L)
                .email(email)
                .password(endcodedPwd)
                .dateCreated(LocalDateTime.now())
                .role(Role.MEMBER)
                .status(UserStatus.ACTIVE)
                .build();
        SigninRequestDto signinRequestDto = new SigninRequestDto(email, "wrongpwd");

        // When
        Mockito.when(memberRepository.findByEmail(email)).thenReturn(Optional.of(findUser)); // 로그인 DB 조회

        // Then
        assertThrows(BadRequestException.class, () -> memberService.signin(signinRequestDto));
    }

    @Test
    @DisplayName("로그인 실패 존재하지 않는 이매일")
    void signinFailByEmail() {

        // Given
        String email = "email@email.com";
        String pwd = "pwd";
        SigninRequestDto signinRequestDto = new SigninRequestDto(email, pwd);

        // When
        Mockito.when(memberRepository.findByEmail(email)).thenReturn(Optional.empty()); // 로그인 DB 조회시 없는 경우

        // Then
        assertThrows(UnauthorizedException.class, () -> memberService.signin(signinRequestDto)); // 이메일이 존재하지 않습니다
    }

    @Test
    @DisplayName("로그인 실패 회원탈퇴")
    void signinFailByLeave() {

        // Given
        String email = "email@email.com";
        String pwd = "pwd";
        String endcodedPwd = passwordEncoder.encode(pwd);
        SigninRequestDto signinRequestDto = new SigninRequestDto(email, pwd);
        User findUser = User.builder()
                .id(1L)
                .email(email)
                .password(endcodedPwd)
                .dateCreated(LocalDateTime.now())
                .role(Role.MEMBER)
                .status(UserStatus.LEAVE)
                .build();
        // When
        Mockito.when(memberRepository.findByEmail(email)).thenReturn(Optional.of(findUser));

        // Then
        assertThrows(UnauthorizedException.class, () -> memberService.signin(signinRequestDto)); // 탈퇴한 회원입니다
    }

    @Test
    @DisplayName("getUserInfo 성공")
    public void getUserInfoSuccess() {
        // Given
        String email = "email@email.com";
        String pwd = "pwd";
        String endcodedPwd = passwordEncoder.encode(pwd);
        User user = User.builder()
                .id(1L)
                .email(email)
                .password(endcodedPwd)
                .dateCreated(LocalDateTime.now())
                .role(Role.MEMBER)
                .status(UserStatus.ACTIVE)
                .build();
        // When
        Mockito.when(memberRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Then
        assertDoesNotThrow(() -> memberService.getUserInfo(email));
    }

    @Test
    @DisplayName("getUserInfo 실패 이메일")
    public void getUserInfoFailByEmail() {
        // Given
        String email = "email@email.com";
        // When
        Mockito.when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Then
        assertThrows(BadRequestException.class, () -> memberService.getUserInfo(email));
    }

    @Test
    @DisplayName("getUserInfo 실패 status")
    public void getUserInfoFailByStatus() {
        // Given
        String email = "email@email.com";
        String pwd = "pwd";
        String endcodedPwd = passwordEncoder.encode(pwd);
        User user = User.builder()
                .id(1L)
                .email(email)
                .password(endcodedPwd)
                .dateCreated(LocalDateTime.now())
                .role(Role.MEMBER)
                .status(UserStatus.LEAVE)
                .build();
        // When
        Mockito.when(memberRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Then
        assertThrows(UnauthorizedException.class, () -> memberService.getUserInfo(email));
    }

    @Test
    @DisplayName("updateToken 성공")
    public void updateTokenSuccess() {
        // given
        String email = "email@email.com";
        String pwd = "pwd";
        String endcodedPwd = passwordEncoder.encode(pwd);
        User user = new User(
                1L,
                Role.MEMBER,
                email,
                "username",
                endcodedPwd,
                LocalDateTime.now(),
                UserStatus.ACTIVE
        );

        // when
        String accessToken = jwtUtils.createAccessToken(user.getId(), user.getEmail(), user.getRole(), user.getStatus());
        String refreshToken = jwtUtils.createRefreshToken();
        //redis에 refreshToken 저장
        Mockito.when(redisUtils.getData(user.getEmail())).thenReturn(refreshToken);
        Mockito.when(memberRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // then
        SigninResponseDto signinResponseDto = memberService.updateToken(new UpdateTokenRequestDto(accessToken, refreshToken));
        Assertions.assertInstanceOf(String.class, signinResponseDto.getAccessToken());
        Assertions.assertInstanceOf(String.class, signinResponseDto.getRefreshToken());
        log.info("AccessToken : {}", signinResponseDto.getAccessToken());
        log.info("RefreshToken : {}", signinResponseDto.getRefreshToken());
    }

    @Test
    @DisplayName("updateToken 실패 refresh token 유효하지 않음")
    public void updateTokenFailByRefreshToken() {
        // given
        String email = "email@email.com";
        String pwd = "pwd";
        String endcodedPwd = passwordEncoder.encode(pwd);
        User user = new User(
                1L,
                Role.MEMBER,
                email,
                "username",
                endcodedPwd,
                LocalDateTime.now(),
                UserStatus.ACTIVE
        );

        // when
        String accessToken = jwtUtils.createAccessToken(user.getId(), user.getEmail(), user.getRole(), user.getStatus());
        String refreshToken = jwtUtils.createRefreshToken();
        //redis에 refreshToken 저장
        Mockito.when(redisUtils.getData(user.getEmail())).thenReturn(refreshToken);
        Mockito.when(memberRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        String wrongRefreshToken = refreshToken + "asd";
        // then
        assertThrows(UnauthorizedException.class, () -> memberService.updateToken(new UpdateTokenRequestDto(accessToken, wrongRefreshToken)));
    }


    @Test
    @DisplayName("updateToken 실패 토큰 id 불일치")
    public void updateTokenFailByIdNotMatch() {
        // given
        String email = "email@email.com";
        String pwd = "pwd";
        String endcodedPwd = passwordEncoder.encode(pwd);
        User user = new User(
                1L,
                Role.MEMBER,
                email,
                "username",
                endcodedPwd,
                LocalDateTime.now(),
                UserStatus.ACTIVE
        );
        // when
        String accessToken = jwtUtils.createAccessToken(user.getId(), user.getEmail(), user.getRole(), user.getStatus());
        String refreshToken = jwtUtils.createRefreshToken();
        String wrongRefreshToken = "wrongRefreshToken";
        //redis에 refreshToken 저장
        Mockito.when(redisUtils.getData(user.getEmail())).thenReturn(refreshToken);
        Mockito.when(memberRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        // then
        assertThrows(UnauthorizedException.class, () -> memberService.updateToken(new UpdateTokenRequestDto(accessToken, wrongRefreshToken)));
    }

    @Test
    @DisplayName("updateToken 실패 토큰 만료")
    public void updateTokenFailByUnmatchedToken() {
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
        //redis에 refreshToken 저장
        Mockito.when(redisUtils.getData(user.getEmail())).thenReturn(null); // 만료되어 redis에서 토큰 삭제
        Mockito.when(memberRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // then
        assertThrows(UnauthorizedException.class, () -> memberService.updateToken(new UpdateTokenRequestDto(accessToken, refreshToken)));
    }
}