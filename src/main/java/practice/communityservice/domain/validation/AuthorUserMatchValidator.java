package practice.communityservice.domain.validation;

import lombok.RequiredArgsConstructor;
import practice.communityservice.domain.exceptions.BadRequestException;
import practice.communityservice.domain.exceptions.ErrorCode;

@RequiredArgsConstructor
public class AuthorUserMatchValidator extends AbstractValidator{
    private final Long userId;
    private final Long authorId;

    @Override
    public void validate() {
        if(userId != authorId){
            throw new BadRequestException(
                    ErrorCode.INVALID_USER_DATA_REQUEST,
                    "게시물 생성 권한이 없습니다."
            );
        }
    }
}
