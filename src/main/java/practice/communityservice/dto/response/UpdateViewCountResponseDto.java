package practice.communityservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import practice.communityservice.dto.request.UpdateViewCountRequestDto;

@Getter
@AllArgsConstructor
public class UpdateViewCountResponseDto {
    private int viewCount;
}
