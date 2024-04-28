package practice.communityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import practice.communityservice.domain.Member;
import practice.communityservice.domain.model.enums.Role;
import practice.communityservice.domain.model.enums.UserStatus;

import java.util.Date;

@Getter @Setter
@AllArgsConstructor
public class SignupResponseDto {
    private Long id;
    private Role role;
    private String email;
    private String username;
    private Date dateCreated;
    private UserStatus status;

    public static SignupResponseDto from(Member member){
        return new SignupResponseDto(
                member.getId(),
                member.getRole(),
                member.getEmail(),
                member.getUsername(),
                member.getDateCreated(),
                member.getStatus()
        );
    }
}
