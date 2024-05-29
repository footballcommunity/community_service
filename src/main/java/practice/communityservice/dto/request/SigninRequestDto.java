package practice.communityservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SigninRequestDto {
    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
