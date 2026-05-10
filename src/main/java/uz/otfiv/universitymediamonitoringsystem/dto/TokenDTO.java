package uz.otfiv.universitymediamonitoringsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.otfiv.universitymediamonitoringsystem.entity.Role;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDTO {
    private String token;
//    private String refreshToken;
    private List<Role> role;
}
