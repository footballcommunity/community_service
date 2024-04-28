package practice.communityservice.domain.model.enums;

import lombok.Getter;

@Getter
public enum Role {
    MEMBER("MEMBER"),
    ADMIN("ADMIN");

    private final String value;

    Role(String value) {
        this.value = value;
    }
}
