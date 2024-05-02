package practice.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.communityservice.domain.validation.AuthorUserMatchValidator;
import practice.communityservice.domain.validation.ObjectNotNullValidator;
import practice.communityservice.domain.validation.ValidatorBucket;
import practice.communityservice.dto.*;
import practice.communityservice.repository.ArticleRepository;
import practice.communityservice.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public GetArticleResponseDto getArticle(long articleId) {
        Optional<ArticleDto> article = articleRepository.getArticleById(articleId);
        List<CommentDto> commentList = commentRepository.getCommentByArticleId(articleId);

        ValidatorBucket validatorBucket = ValidatorBucket.of()
                .consistOf(new ObjectNotNullValidator(article));
        validatorBucket.validate();

        return GetArticleResponseDto.from(article.get(), commentList);
    }

    public PostArticleResponseDto postArticle(PostArticleRequestDto postArticleRequestDto, Long categoryId, Long loginUserId) {
        Long authorId = postArticleRequestDto.getAuthorId();
        String title = postArticleRequestDto.getTitle();
        String content = postArticleRequestDto.getContent();
        Long postId = articleRepository.postArticle(authorId, categoryId, title, content);
        //Validation
        ValidatorBucket validatorBucket = ValidatorBucket.of()
                .consistOf(new AuthorUserMatchValidator(loginUserId, authorId));
        validatorBucket.validate();

        return new PostArticleResponseDto(postId);
    }
    // 게시물 조회 수
    public void updateViewCount(long articleId){

    }
    // 게시물 좋아요
    public void postGoodArticle(long userId, long articleId){

    }
    // 게시물 싫어요
    public void postBadArticle(long userId, long articleID){

    }
}
