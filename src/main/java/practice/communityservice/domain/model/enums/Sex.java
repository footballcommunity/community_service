package practice.communityservice.domain.model.enums;

import lombok.Getter;

@Getter
public enum Sex {
    BOTH(0),
    MAN(1),
    WOMAN(-1),
    ALL(2);
    private final int value;

    Sex(int value){
        this.value = value;
    }

}
