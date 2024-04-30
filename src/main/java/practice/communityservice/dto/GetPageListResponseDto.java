package practice.communityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import practice.communityservice.domain.model.Article;
import java.util.List;

@Getter @Setter
@Builder
public class GetPageListResponseDto {
    private PageDto page;
    private List<Article> articleList;
}
