package practice.communityservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import practice.communityservice.domain.model.enums.MatchStatus;
import practice.communityservice.domain.model.enums.MatchType;
import practice.communityservice.domain.model.enums.Sex;

import java.time.LocalDateTime;

@Getter
public class PostMatchRequestDto {
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime time;
    private String address;
    private int price;
    private String info;
    private MatchStatus status;
    private String link;
    private int sex;
    private MatchType type;
    private Long authorId;
    private int totalCnt;
    private int currnetCnt;
}
