package practice.communityservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import practice.communityservice.dto.GetPageListRequestDto;
import practice.communityservice.dto.GetPageListResponseDto;
import practice.communityservice.service.BoardService;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // page:요청 페이지, pageSize: 한 화면에 출력되는 게시글 수, blockSize: 한 화면에 출력되는 페이지 인덱스 수
    @GetMapping()
    public GetPageListResponseDto getAllPageList(@RequestParam(required = false, defaultValue = "1") int page,
                                                 @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                 @RequestParam(required = false, defaultValue = "5") int blockSize){
        return boardService.getArticleList(page, pageSize, blockSize);
    }

    @GetMapping("/{categoryId}")
    public GetPageListResponseDto getCategoryPageList(@RequestParam(required = false, defaultValue = "1") int page,
                                                      @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                      @RequestParam(required = false, defaultValue = "5") int blockSize,
                                                      @PathVariable Long categoryId){
        return boardService.getCategoryArticleList(page, pageSize, blockSize, categoryId);
    }
}
