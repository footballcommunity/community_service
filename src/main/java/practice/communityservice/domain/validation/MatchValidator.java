package practice.communityservice.domain.validation;

import lombok.RequiredArgsConstructor;
import practice.communityservice.domain.exceptions.BadRequestException;
import practice.communityservice.domain.exceptions.BaseRuntimeException;
import practice.communityservice.domain.exceptions.ErrorCode;

@RequiredArgsConstructor
public class MatchValidator<T> extends AbstractValidator{
    private final T objectA;
    private final T objectB;

    private final BaseRuntimeException exception;
    @Override
    public void validate() {
        if(!objectA.equals(objectB)){
            throw exception;
        }
    }
}
