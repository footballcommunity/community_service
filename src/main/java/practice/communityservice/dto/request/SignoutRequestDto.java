package practice.communityservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignoutRequestDto {
    private String accessToken;
    private String refreshToen;
}
