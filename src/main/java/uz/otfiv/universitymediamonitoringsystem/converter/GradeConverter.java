package uz.otfiv.universitymediamonitoringsystem.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.http.ResponseEntity;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;

@Converter(autoApply = true)
public class GradeConverter implements AttributeConverter<Grade, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Grade grade) {
        if (grade == null) {
            return null;
        }
        return grade.getValue();
    }

    @Override
    public Grade convertToEntityAttribute(Integer integer) {
        if (integer == null) {
            return null;
        }

        ResponseEntity<?> responseEntity = Grade.fromValue(integer);
        if (responseEntity == null) {
            return null;
        }

        return (Grade) responseEntity.getBody();
    }
}
