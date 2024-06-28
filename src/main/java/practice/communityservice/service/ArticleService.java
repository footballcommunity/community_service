package practice.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import practice.communityservice.domain.model.Comment;
import practice.communityservice.domain.model.UserDetails;
import practice.communityservice.domain.validation.AuthorUserMatchValidator;
import practice.communityservice.domain.validation.EffectedRowsValidator;
import practice.communityservice.domain.validation.ObjectNotNullValidator;
import practice.communityservice.domain.validation.ValidatorBucket;
import practice.communityservice.dto.*;
import practice.communityservice.dto.request.PostArticleRequestDto;
import practice.communityservice.dto.request.UpdateViewCountRequestDto;
import practice.communityservice.dto.response.GetArticleResponseDto;
import practice.communityservice.dto.response.PostArticleResponseDto;
import practice.communityservice.repository.ArticleRepository;
import practice.communityservice.repository.CommentRepository;

import java.util.*;

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

        List<Comment> commentDomainList = new ArrayList<>();
        Map<Long, Comment> commentMap = new HashMap<>();
        // parent_id 로 자식 리스트 생성
        // commentList는 parent_id (오름차순)으로 정렬되어 있음 (null first)
        commentList.forEach((commentDto)-> {
            // 부모 comment
            if (commentDto.getParentId() == null) {
                Comment comment = Comment.from(commentDto);
                commentDomainList.add(comment);
                commentMap.put(commentDto.getCommentId(),comment);
            }
            // 자식 comment
            else {
                Comment parentComment = commentMap.get(commentDto.getParentId());
                parentComment.getChildren().add(Comment.from(commentDto));
            }
        });
        return GetArticleResponseDto.from(article.get(), commentDomainList);
    }

    public PostArticleResponseDto postArticle(PostArticleRequestDto postArticleRequestDto, Long categoryId, Long loginUserId) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long authorId = principal.getUserId();
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
    public void updateViewCount(UpdateViewCountRequestDto updateViewCountRequestDto){

        Long articleId = updateViewCountRequestDto.getArticleId();
        int rowsEffected = articleRepository.updateViewCount(articleId);

        ValidatorBucket validatorBucket = ValidatorBucket.of()
                .consistOf(new EffectedRowsValidator(1,rowsEffected));
    }
    // 게시물 좋아요
    public void postGoodArticle(long userId, long articleId){

    }
    // 게시물 싫어요
    public void postBadArticle(long userId, long articleID){

    }
}
