package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.otfiv.universitymediamonitoringsystem.converter.GradeConverter;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;

import java.time.LocalDate;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "foreign_materials", indexes = {
        @Index(name = "idx_foreign_materials_is_deleted", columnList = "is_deleted")
})
public class ForeignMaterials extends AbsEntity { // Xorijiy OAVlarda OTM faoliyatiga doir e’lon qilingan materiallar haqida ma’lumot
    private String title; //
    private String illumination; // Yoritish shakli
    @ElementCollection
    @MapKeyColumn(name = "type-media")  // Map'ning key'ini ko'rsatish uchun
    @Column(name = "link")
    private Map<String, String> mediaNameAndLink; // E’lon qilingan OAV/Ijtimoiy tarmoq turi
    private String typeMediaSocial; // Yoritilgan OAV nomi
    private LocalDate publishedDate; //Yoritilgan sanasi

    @Convert(converter = GradeConverter.class)
    private Grade grade;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Admin admin;

}
