package practice.communityservice.dto;

import lombok.Getter;
import lombok.Setter;
import practice.communityservice.domain.model.enums.Role;
import practice.communityservice.domain.model.enums.UserStatus;

import java.util.Date;

@Getter @Setter
public class SignupRequestDto {
    private Role role;
    private String email;
    private String username;
    private String password;
    private UserStatus status;
}
