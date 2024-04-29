package practice.communityservice.domain.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import practice.communityservice.domain.exceptions.BadRequestException;
import practice.communityservice.domain.exceptions.ErrorCode;
import practice.communityservice.domain.model.User;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class EmailPasswordMatchValidator extends AbstractValidator{
    private final Optional<User> foundUser;
    private final String srcPassword;
    @Override
    public void validate() {
        log.debug("ORIGIN PWD={}",foundUser.get().getPassword());
        log.debug("SRC PWD={}",srcPassword);
        if(!foundUser.get().getPassword().equals(srcPassword)){
            throw new BadRequestException(
                    ErrorCode.INVALID_USER_DATA_REQUEST,
                    "비밀번호가 틀립니다."
            );
        }
    }
}
