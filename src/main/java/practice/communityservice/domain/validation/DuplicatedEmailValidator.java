package practice.communityservice.domain.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import practice.communityservice.domain.exceptions.BadRequestException;
import practice.communityservice.domain.exceptions.ErrorCode;
import practice.communityservice.repository.MemberRepository;

@RequiredArgsConstructor
public class DuplicatedEmailValidator extends AbstractValidator{
    private final MemberRepository memberRepository;
    private final String srcEmail;
    @Override
    public void validate() {
        if(memberRepository.findByEmail(this.srcEmail).isPresent()){
            throw new BadRequestException(
                    ErrorCode.ROW_ALREADY_EXIST,
                    "이미 존재하는 이메일입니다."
            );
        }
    }
}
