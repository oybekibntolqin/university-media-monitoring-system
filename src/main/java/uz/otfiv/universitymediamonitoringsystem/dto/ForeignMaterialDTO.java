package uz.otfiv.universitymediamonitoringsystem.dto;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.MapKeyColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ForeignMaterialDTO {
    @NotBlank
    private String title; // E’lon qilingan material sarlavhasi
    @NotBlank
    private String illumination; // Yoritish shakli
    @ElementCollection
    @MapKeyColumn(name = "type-media")  // Map'ning key'ini ko'rsatish uchun
    @Column(name = "link")
    @NotNull
    private Map<String, String> mediaNameAndLink; // E’lon qilingan OAV/Ijtimoiy tarmoq turi
    @NotBlank
    private String typeMediaSocial; // Yoritilgan OAV nomi
    @NotNull
    private LocalDate publishedDate; //Yoritilgan sanasi
}
