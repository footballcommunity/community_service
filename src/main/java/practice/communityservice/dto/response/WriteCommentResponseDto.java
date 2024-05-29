package practice.communityservice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriteCommentResponseDto {
    private Long commmentId;

    public static WriteCommentResponseDto of(Long commentId){
        return WriteCommentResponseDto.builder()
                .commmentId(commentId)
                .build();
    }
}
