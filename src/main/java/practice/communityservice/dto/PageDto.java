package practice.communityservice.dto;

import lombok.Getter;

@Getter
public class PageDto {
    private int page;       //현재 페이지
    private int maxPage;    //전체 페이지
    private int startPage;  //현재 페이지 기준 시작 페이지
    private int endPage;    //현재 페이지 기준 마지막 페이지

    public PageDto(int totalCount, int pageSize, int blockSize, int page){
        this.page = page;
        this.maxPage = (int) Math.ceil((double) totalCount / pageSize);
        this.startPage = (((int)(Math.ceil((double) page / blockSize))) - 1) * blockSize + 1;
                this.endPage = startPage + blockSize - 1;
        if(this.endPage  > this.maxPage){
            this.endPage = this.maxPage;
        }
        if(this.page > this.maxPage){
            this.page = this.maxPage;
        }
    }
}
