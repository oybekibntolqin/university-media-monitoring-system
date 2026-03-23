package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.otfiv.universitymediamonitoringsystem.converter.GradeConverter;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Period;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "media_project", indexes = {
        @Index(name = "idx_media_project_is_deleted", columnList = "is_deleted")
})
public class MediaProject extends AbsEntity {
    @Column(columnDefinition = "text")
    private String name;
    private String description;
    private String massMedia;
    @Enumerated(EnumType.STRING)
    private Period period;
    private String link;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Admin admin;
    @Convert(converter = GradeConverter.class)
    private Grade grade;
}