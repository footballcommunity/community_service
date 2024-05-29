package practice.communityservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import practice.communityservice.domain.model.UserDetails;
import practice.communityservice.dto.request.SigninRequestDto;
import practice.communityservice.dto.request.SignupRequestDto;
import practice.communityservice.dto.response.SigninResponseDto;
import practice.communityservice.dto.response.SignupResponseDto;
import practice.communityservice.dto.response.UserInfoResponseDto;
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
    // 유저 정보 조회
    @GetMapping("/info")
    UserInfoResponseDto userInfo() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getEmail();
        return memberService.getUserInfo(email);
    }
    @GetMapping("/not-allowed-test")
    void notAllowedTest(){
    }
    // 회원 정보
}
