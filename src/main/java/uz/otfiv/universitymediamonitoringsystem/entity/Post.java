package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.otfiv.universitymediamonitoringsystem.converter.GradeConverter;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Scale;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "post", indexes = {
        @Index(name = "idx_post_is_deleted", columnList = "is_deleted")
})
public class Post extends AbsEntity {
    private String link;
    private LocalDateTime dateTime;
    private String showedUser;
    private String media;
    private String show;
    private String stuff;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Admin admin;
    @Enumerated(EnumType.STRING)
    private Scale scale;
    private String postType;
    @Convert(converter = GradeConverter.class)
    private Grade grade;
}
