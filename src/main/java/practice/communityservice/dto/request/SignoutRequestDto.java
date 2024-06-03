package practice.communityservice.dto.request;

import lombok.Getter;

@Getter
public class SignoutRequestDto {
    private String accessToken;
    private String refreshToen;
}
