package practice.communityservice.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import practice.communityservice.dto.CommentDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Builder
public class Comment {
    private Long commentId;
    private String username;
    private String content;
    private Date dateCreated;
    private Date dateUpdated;
    private List<Comment> children;

    public static Comment from(CommentDto commentDto){
        return Comment.builder()
                .commentId(commentDto.getCommentId())
                .username(commentDto.getUsername())
                .content(commentDto.getContent())
                .dateCreated(commentDto.getDateCreated())
                .dateUpdated(commentDto.getDateUpdated())
                .children(new ArrayList<>())
                .build();
    }
}
