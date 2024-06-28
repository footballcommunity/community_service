package practice.communityservice.domain.model.enums;

import lombok.Getter;

@Getter
public enum MatchStatus {
    AVAILABLE("AVAILABLE"),
    HURRY("HURRY"),
    FULL("FULL"),
    RELEASE("RELEASE"),
    ALL("ALL");

    private final String value;

    MatchStatus(String value){
        this.value = value;
    }
}
