package practice.communityservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import practice.communityservice.domain.model.UserDetails;
import practice.communityservice.dto.request.UpdateViewCountRequestDto;
import practice.communityservice.dto.response.GetArticleResponseDto;
import practice.communityservice.dto.request.PostArticleRequestDto;
import practice.communityservice.dto.response.PostArticleResponseDto;
import practice.communityservice.dto.response.UpdateViewCountResponseDto;
import practice.communityservice.service.ArticleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{articleId}")
    public GetArticleResponseDto getArticle(
            @PathVariable long articleId){
        return articleService.getArticle(articleId);
    }

    @PostMapping("/{categoryId}")
    public PostArticleResponseDto postArticle(@RequestBody PostArticleRequestDto postArticleRequestDto,
                                              @PathVariable Long categoryId){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = userDetails.getUserId();
        return articleService.postArticle(postArticleRequestDto, categoryId, loginUserId);
    }

    @PatchMapping("/view")
    public UpdateViewCountResponseDto updateViweCountResponseDto(@RequestBody UpdateViewCountRequestDto updateViewCountRequestDto){
        log.debug(String.valueOf(updateViewCountRequestDto.getViewCount()));
        articleService.updateViewCount(updateViewCountRequestDto);
        return new UpdateViewCountResponseDto(updateViewCountRequestDto.getViewCount());
    }
}
