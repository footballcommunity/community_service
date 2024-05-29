package practice.communityservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import practice.communityservice.domain.model.UserDetails;
import practice.communityservice.dto.response.GetArticleResponseDto;
import practice.communityservice.dto.request.PostArticleRequestDto;
import practice.communityservice.dto.response.PostArticleResponseDto;
import practice.communityservice.service.ArticleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
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
}
