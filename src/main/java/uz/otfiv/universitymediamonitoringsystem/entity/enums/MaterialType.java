package uz.otfiv.universitymediamonitoringsystem.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum MaterialType {
    INFOGRAPHIC("Infografika"),
    AUDIO("Audio"),
    VIDEO("Video");
    private String value;

    public static MaterialType fromValue(String value) {
        for (MaterialType type : MaterialType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
