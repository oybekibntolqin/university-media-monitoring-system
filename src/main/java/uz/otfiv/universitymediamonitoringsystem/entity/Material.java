package uz.otfiv.universitymediamonitoringsystem.entity;


import jakarta.persistence.*;
import lombok.*;
import uz.otfiv.universitymediamonitoringsystem.converter.GradeConverter;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.MaterialType;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "material", indexes = {
        @Index(name = "idx_material_is_deleted", columnList = "is_deleted")
})
public class Material extends AbsEntity {// akustik va vizual materiallar
    private String topic;
    private LocalDate publishDate;
    @Enumerated(EnumType.STRING)
    private MaterialType materialType;
    private String massMedia;
    private String socialMediaName;
    private String link;
    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Admin admin;

    @Convert(converter = GradeConverter.class)
    private Grade grade;
}
