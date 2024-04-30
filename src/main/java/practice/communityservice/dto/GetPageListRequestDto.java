package practice.communityservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetPageListRequestDto {
    private int currentPage;
    private int listSizePerPage;
}
