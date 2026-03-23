package uz.otfiv.universitymediamonitoringsystem.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@Getter
public enum Province {
    ANDIJON("Andijon viloyati"),
    BUXORO("Buxoro viloyati"),
    FARGONA("Farg‘ona viloyati"),
    JIZZAX("Jizzax viloyati"),
    XORAZM("Xorazm viloyati"),
    NAMANGAN("Namangan viloyati"),
    NAVOIY("Navoiy viloyati"),
    QASHQADARYO("Qashqadaryo viloyati"),
    QORAQALPOGISTON("Qoraqalpog‘iston Respublikasi"),
    SAMARQAND("Samarqand viloyati"),
    SIRDARYO("Sirdaryo viloyati"),
    SURXONDARYO("Surxondaryo viloyati"),
    TOSHKENT_VILOYATI("Toshkent viloyati"),
    TOSHKENT("Toshkent shahri");

    private final String key;


    public static ResponseEntity<?> getValue(String key) {
        for (Province province : Province.values()) {
            if (province.key.equalsIgnoreCase(key)) {
                return ResponseEntity.ok(province);
            }
        }
        return ResponseEntity.notFound().build();

    }

    public static String getStringKey(String value) {
        for (Province province : Province.values()) {
            if (province.key.equalsIgnoreCase(value) || province.toString().equalsIgnoreCase(value)) {
                return province.key;
            }
        }
        return null;
    }


}
