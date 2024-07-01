package practice.communityservice.domain.model.enums;

import lombok.Getter;

@Getter
public enum MatchType {
    PLAB("PLAB"),
    IAMGROUND("IAMGROUND"),
    COMMUNITY("COMMUNITY");

    private final String value;

    MatchType(String value) {
        this.value = value;
    }
}
