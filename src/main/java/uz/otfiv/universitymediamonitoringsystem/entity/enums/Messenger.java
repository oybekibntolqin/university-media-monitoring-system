package uz.otfiv.universitymediamonitoringsystem.entity.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum Messenger {
    TELEGRAM("Telegram"),
    INSTAGRAM("Instagram"),
    FACEBOOK("Facebook"),
    YOUTUBE("Youtube"),
    X("X");
    private String value;

    Messenger(String value) {
        this.value = value;
    }

    public static Messenger fromValue(String value) {
        for (Messenger type : Messenger.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

}
