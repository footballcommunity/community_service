package practice.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.communityservice.domain.Member;
import practice.communityservice.domain.model.enums.UserStatus;
import practice.communityservice.dto.SignupRequestDto;
import practice.communityservice.dto.SignupResponseDto;
import practice.communityservice.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private MemberRepository memberRepository;
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        Member newMember = new Member(
                null,
                signupRequestDto.getRole(),
                signupRequestDto.getEmail(),
                signupRequestDto.getUsername(),
                signupRequestDto.getPassword(),
                null,
                UserStatus.ACTIVE
        );
        memberRepository.save(newMember);
        return SignupResponseDto.from(newMember);
    }
}
