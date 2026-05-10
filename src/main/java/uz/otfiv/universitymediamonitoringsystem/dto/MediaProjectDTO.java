package uz.otfiv.universitymediamonitoringsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaProjectDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String massMedia;
    @NotBlank
    private String period;
    @NotBlank
    @URL
    private String link;
}