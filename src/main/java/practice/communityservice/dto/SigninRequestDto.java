package practice.communityservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SigninRequestDto {
    private String email;
    private String password;
}
