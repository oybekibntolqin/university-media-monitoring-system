package uz.otfiv.universitymediamonitoringsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
    @NotBlank
    private String showedMedia;
    private String channel; // kanallar
    @NotBlank
    private String showedUser; // who is shown in tv or web ....
    @NotBlank
    private String stuff;
    @NotNull
    private LocalDateTime time;
    @NotBlank
    private String link;
    @NotBlank
    private String scale; // scale is Xorijiy, Respublika, Mahalliy
    @NotBlank
    private String type; // type of media for example TV,RADIO,WEB.....
}
