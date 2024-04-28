package practice.communityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import practice.communityservice.domain.exceptions.ErrorCode;

@Getter @Setter
@AllArgsConstructor
public class ExceptionDto {
    private ErrorCode errorCode;
    private String message;
}
