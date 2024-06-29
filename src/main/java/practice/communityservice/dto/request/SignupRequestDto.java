package practice.communityservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import practice.communityservice.domain.model.enums.Role;
import practice.communityservice.domain.model.enums.UserStatus;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SignupRequestDto {
    @NotBlank
    private Role role;
    @Email
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private UserStatus status;
}
