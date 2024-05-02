package practice.communityservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import practice.communityservice.domain.model.enums.Role;
import practice.communityservice.domain.model.enums.UserStatus;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class User {
    private Long id;
    private Role role;
    private String email;
    private String username;
    private String password;
    private LocalDateTime dateCreated;
    private UserStatus status;
}
