package practice.communityservice.domain.validation;

import lombok.RequiredArgsConstructor;
import practice.communityservice.domain.exceptions.BadRequestException;
import practice.communityservice.domain.exceptions.ErrorCode;

import java.util.Optional;

@RequiredArgsConstructor
public class ObjectNotNullValidator extends AbstractValidator{

    private final Optional object;

    @Override
    public void validate() {
        if(object.isEmpty()){
            throw new BadRequestException(
                    ErrorCode.ROW_DOES_NOT_EXIST,
                    "데이터가 존재하지 않습니다."
            );
        }
    }
}
