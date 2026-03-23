package uz.otfiv.universitymediamonitoringsystem.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@Getter
public enum Grade {
    GRADE_ONE(1),
    GRADE_TWO(2),
    GRADE_THREE(3),
    GRADE_FOUR(4),
    GRADE_FIVE(5),
    GRADE_SIX(6),
    GRADE_SEVEN(7),
    GRADE_EIGHT(8),
    GRADE_NINE(9),
    GRADE_TEN(10);
    private final Integer value;

    public static ResponseEntity<?> fromValue(Integer value) {
        for (Grade grade : Grade.values()) {
            if (grade.value.equals(value)) {
                return ResponseEntity.ok(grade);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
