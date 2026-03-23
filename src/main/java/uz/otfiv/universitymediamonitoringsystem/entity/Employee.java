package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Employee extends AbsEntity {
    @Embedded
    private Details details;
    @OneToOne
    private User user;
    private String gender;
    @ManyToOne
    private Speciality speciality;
    private Integer rating;
    @OneToOne(cascade = CascadeType.ALL)
    private AttachmentContent attachmentContent;
}
