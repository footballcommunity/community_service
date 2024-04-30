package practice.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.communityservice.config.JwtUtill;
import practice.communityservice.domain.model.User;
import practice.communityservice.domain.validation.DuplicatedEmailValidator;
import practice.communityservice.domain.validation.EmailExistValidator;
import practice.communityservice.domain.validation.EmailPasswordMatchValidator;
import practice.communityservice.domain.validation.ValidatorBucket;
import practice.communityservice.dto.SigninRequestDto;
import practice.communityservice.dto.SigninResponseDto;
import practice.communityservice.dto.SignupRequestDto;
import practice.communityservice.dto.SignupResponseDto;
import practice.communityservice.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
private final MemberRepository memberRepository;
    private final JwtUtill jwtUtil;
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
        return SigninResponseDto.builder()
                .accessToken(jwtUtil.createAccessToken(user.getEmail(), user.getRole(), user.getStatus()))
                .refreshToken(jwtUtil.createRefreshToken())
                .build();
    }
}
