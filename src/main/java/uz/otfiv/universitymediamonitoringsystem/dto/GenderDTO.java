package uz.otfiv.universitymediamonitoringsystem.dto;

import uz.otfiv.universitymediamonitoringsystem.entity.enums.Province;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenderDTO {
    private Province province;
    private Integer male;
    private Integer female;
}
