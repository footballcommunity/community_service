package practice.communityservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import practice.communityservice.dto.SignupRequestDto;
import practice.communityservice.dto.SignupResponseDto;
import practice.communityservice.service.MemberService;

@Controller("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 가입
    @PostMapping("/signup")
    SignupResponseDto signup(SignupRequestDto signupRequestDto){
        return memberService.signup(signupRequestDto);
    }
    // 로그인

    // 회원 정보
}
