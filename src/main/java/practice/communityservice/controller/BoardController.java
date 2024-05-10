package practice.communityservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import practice.communityservice.domain.model.enums.SearchType;

import practice.communityservice.dto.GetPageListResponseDto;
import practice.communityservice.service.BoardService;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    // page:요청 페이지, pageSize: 한 화면에 출력되는 게시글 수, blockSize: 한 화면에 출력되는 페이지 인덱스 수
    @GetMapping()
    public GetPageListResponseDto getAllPageList(@RequestParam(required = false, defaultValue = "1") int page,
                                                 @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                 @RequestParam(required = false, defaultValue = "5") int blockSize){
        return boardService.getArticleList(page, pageSize, blockSize);
    }

    @GetMapping("/search")
    public GetPageListResponseDto getSearchedPageList(@RequestParam SearchType searchType,
                                    @RequestParam String keyword,
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(required = false, defaultValue = "5") int blockSize){
        return boardService.getSearchedPageList(page, pageSize, blockSize, searchType, keyword);
    }
    @GetMapping("/{categoryId}/search")
    public GetPageListResponseDto getCategorySearchedPageList(@RequestParam String searchType,
                                    @RequestParam String keyword,
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(required = false, defaultValue = "5") int blockSize,
                                    @PathVariable long categoryId){

        return boardService.getCategorySearchedPageList(page, pageSize, blockSize, categoryId, SearchType.valueOf(searchType), keyword);
    }

    @GetMapping("/{categoryId}")
    public GetPageListResponseDto getCategoryPageList(@RequestParam(required = false, defaultValue = "1") int page,
                                                      @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                      @RequestParam(required = false, defaultValue = "5") int blockSize,
                                                      @PathVariable Long categoryId){
        return boardService.getCategoryArticleList(page, pageSize, blockSize, categoryId);
    }
}
