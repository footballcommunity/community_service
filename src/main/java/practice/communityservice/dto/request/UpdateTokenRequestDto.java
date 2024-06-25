package practice.communityservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateTokenRequestDto {
    private String accessToken;
    private String refreshToken;
}
