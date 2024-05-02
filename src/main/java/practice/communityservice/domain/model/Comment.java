package practice.communityservice.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class Comment {
    private Long id;
    private String userName;
    private String content;
    private List<Comment> children;
    private String dateCreated;
}
