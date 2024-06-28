package practice.communityservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostArticleRequestDto {
    @NotEmpty(message = "제목을 입력해 주세요")
    private String title;
    @NotEmpty(message = "내용을 입력해 주세요")
    private String content;
}
