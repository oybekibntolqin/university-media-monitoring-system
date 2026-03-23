package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.OrgType;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Province;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Organization extends AbsEntity {
    private String name;
    @Enumerated(EnumType.STRING)
    private OrgType orgType;
    @Enumerated(EnumType.STRING)
    private Province province;
    private Integer allEmployeesAmount;//rasman ishlaydigan barcha
    private Integer employeesOfAdministrationAmount;
    private Integer professorTeachersAmount;
    private Integer allStudentsAmount;//hamma studentlar(doktorant, magistr,kechki,kunduzgi, sirtqi,masofaviy)

    @Builder.Default
    private Integer rating = 0;
}