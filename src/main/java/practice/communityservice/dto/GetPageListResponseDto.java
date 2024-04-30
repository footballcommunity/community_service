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
    private int page;       //현재 페이지
    private int maxPage;    //전체 페이지
    private int startPage;  //현재 페이지 기준 시작 페이지
    private int endPage;    //현재 페이지 기준 마지막 페이지
    private List<Article> articleList;
}
