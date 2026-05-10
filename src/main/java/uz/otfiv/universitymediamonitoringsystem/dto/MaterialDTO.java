package uz.otfiv.universitymediamonitoringsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class MaterialDTO {
    @NotBlank
    private String topic;
    @NotNull
    private LocalDate publishDate;
    @NotBlank
    private String materialType;
    @NotBlank
    private String massMedia;
    @NotBlank
    private String socialMediaName;
    @NotBlank
    private String link;
}
