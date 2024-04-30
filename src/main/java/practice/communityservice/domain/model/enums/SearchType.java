package practice.communityservice.domain.model.enums;

import lombok.Getter;

@Getter
public enum SearchType {
    TITLE("TITLE"),
    AUTHOR("AUTHOR"),
    CONTENT("CONTENT");

    private final String value;

    SearchType(String value) {
        this.value = value;
    }

}
