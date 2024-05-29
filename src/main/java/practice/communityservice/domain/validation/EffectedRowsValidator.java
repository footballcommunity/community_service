package practice.communityservice.domain.validation;

import lombok.RequiredArgsConstructor;
import practice.communityservice.domain.exceptions.BadRequestException;
import practice.communityservice.domain.exceptions.ErrorCode;

@RequiredArgsConstructor
public class EffectedRowsValidator extends AbstractValidator{

    private final int target;
    private final int rowNum;
    @Override
    public void validate() {
        if(rowNum != target){
            throw new BadRequestException(
                    ErrorCode.ROW_DOES_NOT_EXIST,
                    "존재하지 않는 게시물 입니다."
            );
        }
    }
}
