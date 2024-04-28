package practice.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.communityservice.domain.validation.DuplicatedEmailValidator;
import practice.communityservice.domain.validation.ValidatorBucket;
import practice.communityservice.dto.SignupRequestDto;
import practice.communityservice.dto.SignupResponseDto;
import practice.communityservice.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        ValidatorBucket validatorBucket = ValidatorBucket.of().consistOf(
                new DuplicatedEmailValidator(memberRepository, signupRequestDto.getEmail())
        );
        validatorBucket.validate();
        Long id = memberRepository.save(signupRequestDto);
        return new SignupResponseDto(id);
    }
}
