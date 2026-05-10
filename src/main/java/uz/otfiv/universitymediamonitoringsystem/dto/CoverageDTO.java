package uz.otfiv.universitymediamonitoringsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoverageDTO {
    @NotBlank
    private String eventName;
    @NotBlank
    private String eventType;
    @NotBlank
    private String massMedia;
    @NotBlank
    private String publishType;
    @NotNull(message = "Date cannot be null")
    private LocalDate publishedDate;
    //key: yoritilgan OAV nomi
    //value: havola
    @NotEmpty
    private Map<@NotBlank String, @URL String> mediaLinks;
}
