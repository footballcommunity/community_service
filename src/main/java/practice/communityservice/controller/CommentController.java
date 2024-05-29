package practice.communityservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import practice.communityservice.domain.model.UserDetails;
import practice.communityservice.dto.request.WriteCommentRequestDto;
import practice.communityservice.dto.response.WriteCommentResponseDto;
import practice.communityservice.service.CommentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public WriteCommentResponseDto writeComment(@PathVariable long boardId, @RequestBody WriteCommentRequestDto writeCommentRequestDto){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getUserId();
        return commentService.writeComment(userId, writeCommentRequestDto);
    }

}
