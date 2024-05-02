package practice.communityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@Setter
public class ArticleDto {
    private Long articleId;
    private String authorName;
    private String categoryName;
    private String title;
    private String content;
    private int viewCount;
    private Date dateCreated;
    private Date dateUpdated;
}
