package practice.communityservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import practice.communityservice.domain.model.enums.Role;
import practice.communityservice.domain.model.enums.UserStatus;
import practice.communityservice.dto.request.SignupRequestDto;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private Role role;
    private String email;
    private String username;
    private String password;
    private LocalDateTime dateCreated;
    private UserStatus status;

    public static User from(SignupRequestDto signupRequestDto){
        return User.builder()
                .role(signupRequestDto.getRole())
                .email(signupRequestDto.getEmail())
                .username(signupRequestDto.getUsername())
                .password(signupRequestDto.getPassword())
                .status(signupRequestDto.getStatus())
                .build();
    }
}
