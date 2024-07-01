package practice.communityservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import practice.communityservice.domain.model.enums.MatchStatus;
import practice.communityservice.domain.model.enums.MatchType;
import practice.communityservice.domain.model.enums.Sex;
import practice.communityservice.dto.request.PostMatchRequestDto;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
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
    private MatchType type;
    private Long authorId;
    private int totalCnt;
    private int currnetCnt;

    public static Match from(PostMatchRequestDto postMatchRequestDto) {
        return Match.builder()
                .title(postMatchRequestDto.getTitle())
                .time(postMatchRequestDto.getTime())
                .address(postMatchRequestDto.getAddress())
                .price(postMatchRequestDto.getPrice())
                .info(postMatchRequestDto.getInfo())
                .status(postMatchRequestDto.getStatus())
                .link(postMatchRequestDto.getLink())
                .sex(postMatchRequestDto.getSex())
                .type(postMatchRequestDto.getType())
                .authorId(postMatchRequestDto.getAuthorId())
                .totalCnt(postMatchRequestDto.getTotalCnt())
                .currnetCnt(postMatchRequestDto.getCurrnetCnt())
                .build();
    }
}
