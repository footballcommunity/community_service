package practice.communityservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import practice.communityservice.domain.model.Match;
import practice.communityservice.dto.PageDto;

import java.util.List;

@Getter
@Setter
@Builder
public class GetMatchListResponseDto {
    private PageDto page;
    private List<Match> matchList;
}
