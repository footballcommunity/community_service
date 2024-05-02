package practice.communityservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class Page {
    private Long articleId;
    private String authorName;
    private String categoryName;
    private String title;
    private int viewCount;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
}
