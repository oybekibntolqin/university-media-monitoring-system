package uz.otfiv.universitymediamonitoringsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficialPageDTO {

    private String telegramLink;
    private String instagramLink;
    @NotBlank
    private String link;
    private String youtubeLink;
}
