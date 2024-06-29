package practice.communityservice.domain.validation;

import lombok.RequiredArgsConstructor;
import practice.communityservice.domain.exceptions.BadRequestException;
import practice.communityservice.domain.exceptions.ErrorCode;
import practice.communityservice.domain.exceptions.UnauthorizedException;
import practice.communityservice.domain.model.User;
import practice.communityservice.domain.model.enums.UserStatus;

import java.util.Optional;

@RequiredArgsConstructor
public class UserStateValidator extends AbstractValidator {
    private final UserStatus status;

    @Override
    public void validate() {
        if (status == UserStatus.LEAVE) {
            throw new UnauthorizedException(
                    ErrorCode.LEAVED_USER,
                    "탈퇴한 유저 입니다"
            );
        }
    }
}
