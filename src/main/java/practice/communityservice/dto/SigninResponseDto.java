package practice.communityservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class SigninResponseDto {
    private String accessToken;
    private String refreshToken;
}
