package practice.communityservice.repository;

import practice.communityservice.domain.model.User;
import practice.communityservice.dto.SignupRequestDto;

import java.util.Optional;

public interface MemberRepository {
    // 회원 저장
    Long save(SignupRequestDto signupRequestDto);
    Optional<User> findByEmail(String srcEmail);
}
