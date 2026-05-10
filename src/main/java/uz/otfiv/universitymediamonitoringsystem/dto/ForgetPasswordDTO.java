package uz.otfiv.universitymediamonitoringsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForgetPasswordDTO {
    private String token;
    private String newPassword;
    private String rePassword;
}
