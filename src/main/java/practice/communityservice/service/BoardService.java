package practice.communityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import practice.communityservice.config.JwtUtill;
import practice.communityservice.domain.model.Article;
import practice.communityservice.domain.validation.AuthorUserMatchValidator;
import practice.communityservice.domain.validation.ValidatorBucket;
import practice.communityservice.domain.validation.ValueNotZeroValidator;
import practice.communityservice.dto.GetPageListResponseDto;
import practice.communityservice.dto.PostArticleRequestDto;
import practice.communityservice.dto.PostArticleResponseDto;
import practice.communityservice.repository.BoardRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    public GetPageListResponseDto getArticleList(int page, int pageSize, int blockSize){
        // DB
        List<Article> articleList = boardRepository.getPageArticleList(page, pageSize);
        int articleCount = boardRepository.allBoardCount();
        // Validation
        ValidatorBucket validatorBucket = ValidatorBucket.of()
                .consistOf(new ValueNotZeroValidator(pageSize))
                .consistOf(new ValueNotZeroValidator(blockSize));
        validatorBucket.validate();
        // Logic
        int maxPage = (int) Math.ceil((double) articleCount / pageSize);
        int startPage = (((int)(Math.ceil((double) page / blockSize))) - 1) * blockSize + 1;
        int endPage = startPage + blockSize - 1;
        if(endPage  > maxPage){
            endPage = maxPage;
        }

        return GetPageListResponseDto.builder()
                .articleList(articleList)
                .page(page)
                .maxPage(maxPage)
                .startPage(startPage)
                .endPage(endPage)
                .build();
    }

    public GetPageListResponseDto getCategoryArticleList(int page, int pageSize, int blockSize, Long categoryId) {
        List<Article> categoryArticleList = boardRepository.getPageCategoryArticleList(page, pageSize, categoryId);
        int categoryArticleCount = boardRepository.categoryBoardCount(categoryId);
        // Validation
        ValidatorBucket validatorBucket = ValidatorBucket.of()
                .consistOf(new ValueNotZeroValidator(pageSize))
                .consistOf(new ValueNotZeroValidator(blockSize));
        validatorBucket.validate();
        // Logic
        int maxPage = (int) Math.ceil((double) categoryArticleCount / pageSize);
        int startPage = (((int)(Math.ceil((double) page / blockSize))) - 1) * blockSize + 1;
        int endPage = startPage + blockSize - 1;
        if(endPage  > maxPage){
            endPage = maxPage;
        }

        return GetPageListResponseDto.builder()
                .articleList(categoryArticleList)
                .page(page)
                .maxPage(maxPage)
                .startPage(startPage)
                .endPage(endPage)
                .build();
    }

    public PostArticleResponseDto postArticle(PostArticleRequestDto postArticleRequestDto, Long categoryId, Long userId) {
        log.debug("userId={}",userId);
        Long authorId = postArticleRequestDto.getAuthorId();
        String title = postArticleRequestDto.getTitle();
        String content = postArticleRequestDto.getContent();
        Long postId = boardRepository.postArticle(authorId, categoryId, title, content);
        //Validation
        ValidatorBucket validatorBucket = ValidatorBucket.of()
                .consistOf(new AuthorUserMatchValidator(userId, authorId));
        validatorBucket.validate();

        return new PostArticleResponseDto(postId);
    }
}
