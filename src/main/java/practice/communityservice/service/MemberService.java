package practice.communityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import practice.communityservice.domain.exceptions.BadRequestException;
import practice.communityservice.domain.exceptions.ErrorCode;
import practice.communityservice.dto.request.SignoutRequestDto;
import practice.communityservice.dto.response.SignoutResponseDto;
import practice.communityservice.utils.JwtUtils;
import practice.communityservice.domain.model.User;
import practice.communityservice.domain.validation.*;
import practice.communityservice.dto.request.SigninRequestDto;
import practice.communityservice.dto.request.SignupRequestDto;
import practice.communityservice.dto.response.SigninResponseDto;
import practice.communityservice.dto.response.SignupResponseDto;
import practice.communityservice.dto.response.UserInfoResponseDto;
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

    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        Optional<User> foundUser = memberRepository.findByEmail(email);
        ValidatorBucket validatorBucket = ValidatorBucket.of().consistOf(
                new DuplicatedEmailValidator(foundUser)
        );
        validatorBucket.validate();
        Long id = memberRepository.save(signupRequestDto);
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
                .consistOf(new EmailPasswordMatchValidator(foundUser, password));
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

    public SigninResponseDto updateToken(String refreshToken) {
        String email = redisUtils.getData(refreshToken);
        User user = memberRepository.findByEmail(email).orElseThrow(
                () -> new BadRequestException(
                        ErrorCode.ROW_DOES_NOT_EXIST,
                        "RefreshToken 검증 실패"
                )
        );
        // 유저 Status 검증

        String newAccessToken = jwtUtils.createAccessToken(user.getId(),user.getEmail(),user.getRole(), user.getStatus());
        return SigninResponseDto.builder()
                .accessToken(newAccessToken)
                .build();
    }

    public SignoutResponseDto signout(SignoutRequestDto signoutRequestDto) {
        redisUtils.addToBlacklist(signoutRequestDto.getAccessToken());
        redisUtils.deleteData(signoutRequestDto.getRefreshToen());
        return SignoutResponseDto.builder()
                .message("로그아웃 성공")
                .build();
    }
}
