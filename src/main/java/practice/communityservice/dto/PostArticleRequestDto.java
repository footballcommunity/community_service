package practice.communityservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostArticleRequestDto {
    private Long authorId;
    private String title;
    private String content;
}
