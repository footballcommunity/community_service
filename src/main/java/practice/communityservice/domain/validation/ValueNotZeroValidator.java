package practice.communityservice.domain.validation;

import lombok.RequiredArgsConstructor;
import practice.communityservice.domain.exceptions.BadRequestException;
import practice.communityservice.domain.exceptions.ErrorCode;
import practice.communityservice.domain.model.User;

import java.util.Optional;

@RequiredArgsConstructor
public class ValueNotZeroValidator extends AbstractValidator{
    private final Object value;
    @Override
    public void validate() {
        if((int) value == 0){
            throw new BadRequestException(
                    ErrorCode.INVALID_USER_DATA_REQUEST,
                    "0이 될수 없는 값입니다."
            );
        }
    }
}
