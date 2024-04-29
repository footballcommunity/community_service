package practice.communityservice.domain.validation;

import lombok.RequiredArgsConstructor;
import practice.communityservice.domain.exceptions.BadRequestException;
import practice.communityservice.domain.exceptions.ErrorCode;
import practice.communityservice.domain.model.User;

import java.util.Optional;

@RequiredArgsConstructor
public class EmailExistValidator extends AbstractValidator{
    private final Optional<User> foundUser;
    @Override
    public void validate() {
        if(foundUser.isEmpty()){
            throw new BadRequestException(
                    ErrorCode.ROW_DOES_NOT_EXIST,
                    "존재하지 않는 이메일입니다."
            );
        }
    }
}
