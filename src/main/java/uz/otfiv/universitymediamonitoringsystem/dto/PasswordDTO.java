package uz.otfiv.universitymediamonitoringsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDTO {
    private UUID employeeId;
    private String password;
}
