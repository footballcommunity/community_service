package practice.communityservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import practice.communityservice.domain.model.UserDetails;
import practice.communityservice.dto.request.*;
import practice.communityservice.dto.response.*;
import practice.communityservice.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    // 회원 가입
    @PostMapping("/signup")
    SignupResponseDto signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return memberService.signup(signupRequestDto);
    }

    // 로그인
    @PostMapping("/signin")
    SigninResponseDto signin(@RequestBody SigninRequestDto signinRequestDto) {
        return memberService.signin(signinRequestDto);
    }

    @PostMapping("/signout")
    SignoutResponseDto signout(@RequestBody SignoutRequestDto signoutRequestDto) {
        return memberService.signout(signoutRequestDto);
    }

    // 유저 정보 조회
    @GetMapping("/info")
    UserInfoResponseDto userInfo() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getEmail();
        return memberService.getUserInfo(email);
    }

    @PostMapping("/refresh")
    SigninResponseDto updateToken(@RequestBody UpdateTokenRequestDto updateTokenRequestDto) {
        return memberService.updateToken(updateTokenRequestDto);
    }

    @GetMapping("/not-allowed-test")
    void notAllowedTest() {
    }

    // 회원 정보 수정
    @PatchMapping("/username")
    UpdateUsernameResponseDto updateUsername(@RequestBody UpdateUsernameRequestDto updateUsernameRequestDto) {
        return memberService.updateUsername(updateUsernameRequestDto);
    }

    @PatchMapping("/password")
    UpdatePasswordResponseDto updatePassword(@RequestBody UpdatePasswordRequestDto updatePasswordRequestDto) {
        return memberService.updatePassword(updatePasswordRequestDto);
    }

}
