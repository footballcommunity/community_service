package practice.communityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private Long commentId;
    private String username;
    private String content;
    private Date dateCreated;
    private Date dateUpdated;
    private Long parentId;
}
