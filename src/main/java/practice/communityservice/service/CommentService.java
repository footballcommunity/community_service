package practice.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.communityservice.domain.validation.ValidatorBucket;
import practice.communityservice.dto.request.WriteCommentRequestDto;
import practice.communityservice.dto.response.WriteCommentResponseDto;
import practice.communityservice.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public WriteCommentResponseDto writeComment(Long userId, WriteCommentRequestDto writeCommentRequestDto){
        writeCommentRequestDto.setUserId(userId);
        Long commentId = commentRepository.saveComment(writeCommentRequestDto);
        return WriteCommentResponseDto.of(userId);
    }
}
