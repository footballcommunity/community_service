package practice.communityservice.domain.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import practice.communityservice.domain.exceptions.BadRequestException;
import practice.communityservice.domain.exceptions.ErrorCode;
import practice.communityservice.domain.model.User;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class EmailPasswordMatchValidator extends AbstractValidator{
    private final String destPassword;
    private final String srcPassword;
    @Override
    public void validate() {
        log.debug("ORIGIN PWD={}",destPassword);
        log.debug("SRC PWD={}",srcPassword);
        if(!destPassword.equals(srcPassword)){
            throw new BadRequestException(
                    ErrorCode.INVALID_USER_DATA_REQUEST,
                    "비밀번호가 틀립니다."
            );
        }
    }
}
