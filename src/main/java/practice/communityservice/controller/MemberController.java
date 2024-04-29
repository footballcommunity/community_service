package practice.communityservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import practice.communityservice.domain.validation.ValidatorBucket;
import practice.communityservice.dto.SigninRequestDto;
import practice.communityservice.dto.SigninResponseDto;
import practice.communityservice.dto.SignupRequestDto;
import practice.communityservice.dto.SignupResponseDto;
import practice.communityservice.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    // 회원 가입
    @PostMapping("/signup")
    SignupResponseDto signup(@RequestBody SignupRequestDto signupRequestDto){
        return memberService.signup(signupRequestDto);
    }
    // 로그인
    @PostMapping("/signin")
    SigninResponseDto signin(@RequestBody SigninRequestDto signinRequestDto){
        return memberService.signin(signinRequestDto);
    }

    @GetMapping("/not-allowed-test")
    void notAllowedTest(){
    }
    // 회원 정보
}
