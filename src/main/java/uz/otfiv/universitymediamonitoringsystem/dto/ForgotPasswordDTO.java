package uz.otfiv.universitymediamonitoringsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForgotPasswordDTO {
    private String token;
    private String password;
    private String rePassword;
}
