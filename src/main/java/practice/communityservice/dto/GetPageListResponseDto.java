package practice.communityservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import practice.communityservice.domain.model.Page;
import java.util.List;

@Getter @Setter
@Builder
public class GetPageListResponseDto {
    private PageDto page;
    private List<Page> pageList;
}
