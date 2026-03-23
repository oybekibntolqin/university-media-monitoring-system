package uz.otfiv.universitymediamonitoringsystem.entity.enums;

import lombok.Getter;

@Getter
public enum Period {
    KUNLIK("Kunlik"),
    HAFTALIK("Haftalik"),
    OYLIK("Oylik"),
    CHORAKLIK("Choraklik"),
    YILLIK("Yillik");
    private final String name;

    Period(String name) {
        this.name = name;
    }

    public static Period get(String name) {
        for (Period value : Period.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }
}