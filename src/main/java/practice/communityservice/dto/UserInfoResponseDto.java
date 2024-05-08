package practice.communityservice.dto;

import lombok.Builder;
import lombok.Getter;
import practice.communityservice.domain.model.User;
import practice.communityservice.domain.model.enums.Role;
import practice.communityservice.domain.model.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Getter
public class UserInfoResponseDto {
    private String email;
    private String username;
    private LocalDateTime dateCreated;
    private UserStatus status;
    private Role role;
    public static UserInfoResponseDto from(User user) {
        return UserInfoResponseDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .dateCreated(user.getDateCreated())
                .status(user.getStatus())
                .role(user.getRole())
                .build();
    }
}
