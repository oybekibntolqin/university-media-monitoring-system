package uz.otfiv.universitymediamonitoringsystem.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.otfiv.universitymediamonitoringsystem.converter.GradeConverter;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coverage", indexes = {
        @Index(name = "idx_coverage_is_deleted ", columnList = "is_deleted")
})
public class Coverage extends AbsEntity {
    private String eventName;
    private String eventType;
    private String massMedia;
    private String publishType;
    private LocalDate publishDate;
    //key: yoritilgan OAV nomi
    //value: havola
    @ElementCollection
    @CollectionTable(name = "medianame_link")
    @MapKeyColumn
    private Map<String, String> mediaLinks;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Admin admin;
    @Convert(converter = GradeConverter.class)
    private Grade grade;
}