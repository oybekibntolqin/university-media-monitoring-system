package uz.otfiv.universitymediamonitoringsystem.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum MediaEventType {
    PRESS_CONFERENCE("Matbuot_anjumani"), //Matbuot anjumani
    BRIEFING("Brifing"), //Brifing
    PRESS_TOUR("Press_tur"); //PRESS_TUR
    private String value;

    public static MediaEventType fromValue(String value) {
        for (MediaEventType type : MediaEventType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

}
