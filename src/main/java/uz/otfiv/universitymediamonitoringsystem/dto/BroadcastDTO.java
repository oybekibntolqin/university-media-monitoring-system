package uz.otfiv.universitymediamonitoringsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BroadcastDTO {
    @NotBlank
    private String title;
    @NotNull
    private LocalDate eventDate;
    @NotNull
    private List<String> stuffs;
    @NotBlank
    private String messenger;
    @NotNull
    private Integer amount;
    @NotBlank
    private String link;
}
