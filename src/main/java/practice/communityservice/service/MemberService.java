package practice.communityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import practice.communityservice.domain.exceptions.BadRequestException;
import practice.communityservice.domain.exceptions.ErrorCode;
import practice.communityservice.domain.exceptions.UnauthorizedException;
import practice.communityservice.domain.model.UserDetails;
import practice.communityservice.dto.request.*;
import practice.communityservice.dto.response.*;
import practice.communityservice.utils.JwtUtils;
import practice.communityservice.domain.model.User;
import practice.communityservice.domain.validation.*;
import practice.communityservice.repository.MemberRepository;
import practice.communityservice.utils.RedisUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;
    private final PasswordEncoder passwordEncoder;

    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        Optional<User> foundUser = memberRepository.findByEmail(email);
        ValidatorBucket.of()
                .consistOf(new DuplicatedEmailValidator(foundUser))
                .validate();
        User newUser = User.from(signupRequestDto);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Long id = memberRepository.save(newUser);
        return new SignupResponseDto(id);
    }

    public SigninResponseDto signin(SigninRequestDto signinRequestDto) {
        // DB 조회
        User foundUser = memberRepository.findByEmail(signinRequestDto.getEmail()).orElseThrow(
                () -> new UnauthorizedException(
                        ErrorCode.INVALID_SIGNIN,
                        "잘못된 이메일 입니다."
                )
        );
        // Validation
        // 비밀번호 확인, 유저 status 확인
        ValidatorBucket.of()
                .consistOf(new EmailPasswordMatchValidator(passwordEncoder, foundUser.getPassword(), signinRequestDto.getPassword()))
                .consistOf(new UserStateValidator(foundUser.getStatus()))
                .validate();

        String accessToken = jwtUtils.createAccessToken(foundUser.getId(), foundUser.getEmail(), foundUser.getRole(), foundUser.getStatus());
        String refreshToken = jwtUtils.createRefreshToken();
        // refreshToken -> Redis 보관
        redisUtils.setData(foundUser.getEmail(), refreshToken, jwtUtils.getRefreshTokenExpTime());
        return SigninResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public UserInfoResponseDto getUserInfo(String email) {

        //DB 조회
        User foundUser = memberRepository.findByEmail(email).orElseThrow(
                () -> new BadRequestException(
                        ErrorCode.ROW_DOES_NOT_EXIST,
                        "로그인된 사용자를 찾을 수 없습니다."
                )
        );
        // Validation
        ValidatorBucket.of()
                .consistOf(new UserStateValidator(foundUser.getStatus()))
                .validate();

        return UserInfoResponseDto.from(foundUser);
    }

    public SigninResponseDto updateToken(UpdateTokenRequestDto updateTokenRequestDto) {
        // 1. refreshToken의 id 일치 여부 확인, 유효성 확인
        UserDetails userDetails = (UserDetails) jwtUtils.getAuthentication(updateTokenRequestDto.getAccessToken()).getPrincipal();
        String email = userDetails.getEmail();
        if (updateTokenRequestDto.getRefreshToken() != redisUtils.getData(email))
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH, "유효하지 않은 토큰입니다");

        User user = memberRepository.findByEmail(email).orElseThrow(
                () -> new BadRequestException(
                        ErrorCode.ROW_DOES_NOT_EXIST,
                        "사용자를 찾을 수 없습니다"
                )
        );
        ValidatorBucket.of()
                .consistOf(new UserStateValidator(user.getStatus()))
                .validate();
        //2. Token 생성 redis 저장
        String newAccessToken = jwtUtils.createAccessToken(user.getId(), user.getEmail(), user.getRole(), user.getStatus());
        String newRefreshToken = jwtUtils.createRefreshToken();
        redisUtils.setData(user.getEmail(), newRefreshToken, jwtUtils.getRefreshTokenExpTime());

        return SigninResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public SignoutResponseDto signout(SignoutRequestDto signoutRequestDto) {
        redisUtils.addToBlacklist(signoutRequestDto.getAccessToken());
        redisUtils.deleteData(signoutRequestDto.getRefreshToen());
        return SignoutResponseDto.builder()
                .message("로그아웃 성공")
                .build();
    }

    public UpdateUsernameResponseDto updateUsername(UpdateUsernameRequestDto updateUsernameRequestDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getEmail();
        memberRepository.updateUsername(email, updateUsernameRequestDto.getUsername());
        return new UpdateUsernameResponseDto(updateUsernameRequestDto.getUsername());
    }

    public UpdatePasswordResponseDto updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getEmail();
        memberRepository.updatePassword(email, passwordEncoder.encode(updatePasswordRequestDto.getPassword()));
        return new UpdatePasswordResponseDto(email);
    }
}
