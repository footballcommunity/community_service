package practice.communityservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import practice.communityservice.domain.model.enums.Role;
import practice.communityservice.domain.model.enums.UserStatus;

import java.util.Date;

@Getter @Setter
@AllArgsConstructor
public class Member {
    private Long id;
    private Role role;
    private String email;
    private String username;
    private String password;
    private Date dateCreated;
    private UserStatus status;
}
