package practice.communityservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import practice.communityservice.domain.model.enums.Role;
import practice.communityservice.domain.model.enums.UserStatus;

@Getter
@AllArgsConstructor
public class UserDetails {
    private Long userId;
    private String email;
    private Role role;
    private UserStatus status;
}
