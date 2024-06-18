package practice.communityservice.domain.model;


import practice.communityservice.domain.model.enums.MatchStatus;
import practice.communityservice.domain.model.enums.Sex;

import java.time.LocalDateTime;

public class Match {
    private Long id;
    private String title;
    private LocalDateTime time;
    private String address;
    private int price;
    private String info;
    private MatchStatus status;
    private String link;
    private Sex sex;
}
