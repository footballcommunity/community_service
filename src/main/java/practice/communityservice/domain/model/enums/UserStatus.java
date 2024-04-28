package practice.communityservice.domain.model.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("ACTIVE"),
    LEAVE("LEAVE");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }
}
