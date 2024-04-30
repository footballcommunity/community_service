package practice.communityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import practice.communityservice.domain.model.Article;
import practice.communityservice.domain.model.enums.SearchType;
import practice.communityservice.domain.validation.AuthorUserMatchValidator;
import practice.communityservice.domain.validation.ValidatorBucket;
import practice.communityservice.domain.validation.ValueNotZeroValidator;
import practice.communityservice.dto.GetPageListResponseDto;
import practice.communityservice.dto.PageDto;
import practice.communityservice.dto.PostArticleRequestDto;
import practice.communityservice.dto.PostArticleResponseDto;
import practice.communityservice.repository.BoardRepository;

import java.util.ArrayList;
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
        PageDto pageDto = new PageDto(articleCount, pageSize, blockSize, page);
        return GetPageListResponseDto.builder()
                .page(pageDto)
                .articleList(articleList)
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
        PageDto pageDto = new PageDto(categoryArticleCount, pageSize, blockSize, page);
        return GetPageListResponseDto.builder()
                .page(pageDto)
                .articleList(categoryArticleList)
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

    public GetPageListResponseDto getSearchedPageList(int page, int pageSize, int blockSize, SearchType searchType, String keyword) {
        List<Article> searchedPageList = new ArrayList<>();
        int totalCount = 0;
        // searchTyp 1.제목 2.작성자 3.내용
        switch (searchType){
            case TITLE -> {
                searchedPageList = boardRepository.getPageByTitle(page, pageSize, keyword);
                totalCount = boardRepository.getPageCountByTitle(keyword);
            }
            case AUTHOR -> {
                searchedPageList = boardRepository.getPageByAuthor(page, pageSize, keyword);
                totalCount = boardRepository.getPageCountByAuthor(keyword);
            }
            case CONTENT -> {
                searchedPageList=boardRepository.getPageByContent(page, pageSize, keyword);
                totalCount = boardRepository.getPageCountByContent(keyword);
            }
        }
        PageDto pageDto = new PageDto(totalCount, pageSize, blockSize, page);
        return GetPageListResponseDto.builder()
                .page(pageDto)
                .articleList(searchedPageList)
                .build();
    }

    public GetPageListResponseDto getCategorySearchedPageList(int page, int pageSize, int blockSize, Long categoryId, SearchType searchType, String keyword) {
        List<Article> searchedPageList = new ArrayList<>();
        int totalCount = 0;
        // searchTyp 1.제목 2.작성자 3.내용
        switch (searchType){
            case TITLE -> {
                searchedPageList = boardRepository.getPageByCategoryAndTitle(page, pageSize, categoryId, keyword);
                totalCount = boardRepository.getPageCountByCategoryAndTitle(categoryId, keyword);
            }
            case AUTHOR -> {
                searchedPageList = boardRepository.getPageByCategoryAndAuthor(page, pageSize, categoryId, keyword);
                totalCount = boardRepository.getPageCountByCategoryAndAuthor(categoryId, keyword);
            }
            case CONTENT -> {
                searchedPageList=boardRepository.getPageByCategoryAndContent(page, pageSize, categoryId, keyword);
                totalCount = boardRepository.getPageCountByCategoryAndContent(categoryId, keyword);
            }
        }
        PageDto pageDto = new PageDto(totalCount, pageSize, blockSize, page);
        return GetPageListResponseDto.builder()
                .page(pageDto)
                .articleList(searchedPageList)
                .build();
    }
}
