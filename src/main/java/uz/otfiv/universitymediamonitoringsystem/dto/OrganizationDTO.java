package uz.otfiv.universitymediamonitoringsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrganizationDTO {
    private Integer allEmployeesAmount;//rasman ishlaydigan barcha
    private Integer employeesOfAdministrationAmount;
    private Integer professorTeachersAmount;
    private Integer allStudentsAmount;//hamma studentlar(doktorant, magistr,kechki,kunduzgi, sirtqi,masofaviy)

}
