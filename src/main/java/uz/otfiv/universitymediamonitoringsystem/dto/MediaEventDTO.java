package uz.otfiv.universitymediamonitoringsystem.dto;

import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaEventDTO {
    @NotBlank
    private String eventName;
    @NotNull
    private LocalDateTime dateOfEvent;
    @NotNull
    @ElementCollection
    private List<String> stuffs;
    private String type;
    private Map<String, String> tv_channels;
    private Map<String, String> radio_channels;
    private Map<String, String> newspapers;
    private Map<String, String> messengers;
    private Map<String, String> web_sites;
}
