package practice.communityservice.dto;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.Setter;
import practice.communityservice.domain.exceptions.ErrorCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ConstraintExceptionDto {
    private final Integer errorCode;
    private final String message;
    private final LocalDateTime timeStamp;
    private final List<String> violations;

    public ConstraintExceptionDto(ErrorCode errorCode, String message, ConstraintViolationException exception) {
        this.errorCode = errorCode.getCode();
        this.message = message;

        List<String> errors = new ArrayList<>();
        exception.getConstraintViolations().forEach(violation ->
                errors.add(violation.getRootBeanClass().getName() + " " +
                        violation.getPropertyPath() + ": " + violation.getMessage()));

        this.violations = errors;
        this.timeStamp = LocalDateTime.now();
    }
}
