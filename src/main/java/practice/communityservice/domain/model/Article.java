package practice.communityservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class Article {
    private Long articleId;
    private String authorName;
    private String categoryName;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
}
