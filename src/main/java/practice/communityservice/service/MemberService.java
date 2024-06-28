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
        ValidatorBucket validatorBucket = ValidatorBucket.of().consistOf(
                new DuplicatedEmailValidator(foundUser)
        );
        validatorBucket.validate();
        User newUser = User.from(signupRequestDto);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Long id = memberRepository.save(newUser);
        return new SignupResponseDto(id);
    }

    public SigninResponseDto signin(SigninRequestDto signinRequestDto) {
        String email = signinRequestDto.getEmail();
        String password = signinRequestDto.getPassword();
        // DB 조회
        Optional<User> foundUser = memberRepository.findByEmail(email);
        // Validation
        ValidatorBucket validatorBucket = ValidatorBucket.of()
                .consistOf(new EmailExistValidator(foundUser))
                .consistOf(new EmailPasswordMatchValidator(passwordEncoder, foundUser.get().getPassword(), password));
        validatorBucket.validate();
        User user = foundUser.get();
        String accessToken = jwtUtils.createAccessToken(user.getId(), user.getEmail(), user.getRole(), user.getStatus());
        String refreshToken = jwtUtils.createRefreshToken();
        redisUtils.setData(user.getEmail(), refreshToken, jwtUtils.getRefreshTokenExpTime());
        return SigninResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public UserInfoResponseDto getUserInfo(String email) {
        log.debug("login user email : " + email);
        //DB 조회
        Optional<User> foundUser = memberRepository.findByEmail(email);
        // Validation
        ValidatorBucket validatorBucket = ValidatorBucket.of()
                .consistOf(new ObjectNotNullValidator(foundUser))
                .consistOf(new EmailExistValidator(foundUser));
        validatorBucket.validate();

        User user = foundUser.get();
        return UserInfoResponseDto.from(user);
    }

    public SigninResponseDto updateToken(UpdateTokenRequestDto updateTokenRequestDto) {
        // 1. refreshToken 유효성 검사
        if(!jwtUtils.validateToken(updateTokenRequestDto.getRefreshToken())) throw new UnauthorizedException(ErrorCode.INVALID_REFRESH, "유효하지 않은 토큰입니다");
        // 2. AccessToken의 id 와 refreshToken의 id 일치
        UserDetails userDetails = (UserDetails) jwtUtils.getAuthentication(updateTokenRequestDto.getAccessToken()).getPrincipal();
        String email = userDetails.getEmail();
        if(updateTokenRequestDto.getRefreshToken() != redisUtils.getData(email)) throw new UnauthorizedException(ErrorCode.INVALID_REFRESH, "유효하지 않은 토큰입니다");

        User user = memberRepository.findByEmail(email).orElseThrow(
                () -> new BadRequestException(
                        ErrorCode.ROW_DOES_NOT_EXIST,
                        "RefreshToken 검증 실패"
                )
        );
        //3. Token 생성 redis 저장
        String newAccessToken = jwtUtils.createAccessToken(user.getId(),user.getEmail(),user.getRole(), user.getStatus());
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
