package uz.otfiv.universitymediamonitoringsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilterEmployeeDTO {
    private String fullName;
    private String phone;

}
