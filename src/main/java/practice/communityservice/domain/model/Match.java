package practice.communityservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import practice.communityservice.domain.model.enums.MatchStatus;
import practice.communityservice.domain.model.enums.Sex;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Match {
    private Long id;
    private String title;
    private LocalDateTime time;
    private String address;
    private int price;
    private String info;
    private MatchStatus status;
    private String link;
    private int sex;
}
