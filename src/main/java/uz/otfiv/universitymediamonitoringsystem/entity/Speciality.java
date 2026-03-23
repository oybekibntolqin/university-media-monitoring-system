package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.MapKeyColumn;
import lombok.*;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Speciality extends AbsEntity {
    @ElementCollection
    @MapKeyColumn(name = "degree")
    @Column(name = "speciality")
    private Map<String, String> specialities;
}
