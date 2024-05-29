package practice.communityservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WriteCommentRequestDto {
    private Long articleId;
    private Long userId;
    private Long parentId;
    private String content;
}
