package uz.otfiv.universitymediamonitoringsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.otfiv.universitymediamonitoringsystem.entity.Post;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeInfoDTO {
    String image;
    String fullName;
    String phone;
    List<Post> posts;
}
