package practice.communityservice.domain.validation;

import lombok.RequiredArgsConstructor;
import practice.communityservice.domain.exceptions.BaseRuntimeException;
import practice.communityservice.utils.JwtUtils;

@RequiredArgsConstructor
public class TokenValidator extends AbstractValidator{
    private final String token;
    private final JwtUtils jwtUtils;
    private final BaseRuntimeException exception;

    @Override
    public void validate() {

        if(!jwtUtils.validateToken(token)){
            throw exception;
        }
    }
}
