package practice.communityservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.communityservice.dto.SignupRequestDto;
import practice.communityservice.dto.SignupResponseDto;
import practice.communityservice.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    // 회원 가입
    @PostMapping("/signup")
    SignupResponseDto signup(@RequestBody SignupRequestDto signupRequestDto){
        return memberService.signup(signupRequestDto);
    }
    // 로그인

    // 회원 정보
}
