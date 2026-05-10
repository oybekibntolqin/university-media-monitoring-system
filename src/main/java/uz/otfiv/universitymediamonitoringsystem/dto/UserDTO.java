package uz.otfiv.universitymediamonitoringsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotNull
    @NotBlank(message = "not null")
    private String fullName;
    @NotBlank
    @NotNull
    private String phone;
    @NotNull
    private Map<String, String> specialities;
    @NotNull
    private LocalDate birthday;
    @NotBlank
    @NotNull
    private String gender;
    @NotBlank
    @NotNull
    private String province;
    @NotNull
    private UUID organizationId;
    @NotBlank
    @NotNull
    private String commendNumber; //buyruq raqami
    @NotNull
    private LocalDate entryDate; // ishga kirgan vaqti
    @NotBlank
    @NotNull
    private String license;  // AOKA Attestatsiyadan o'tganligi.Guvohnoma raqami
    @NotBlank
    @NotNull
    private String qualificationInfo; // AOKA da malaka oshirish
    @NotNull
    private Map<String, String> workType; // shatatdagi o'rni
    @NotNull
    private String averageSalary; // oxirgi 1 yillikdagi o'rtacha oylik
    @NotBlank
    @NotNull
    private String advisor; //maslahatchi etib belgilanganmi yoki yo'qligi. Ha bo'lsa hujjat raqami
    @NotBlank
    @NotNull
    private String departmentOrganisation; // Matubot bo'limi alohidami
    @NotBlank
    @NotNull
    private String room; // Xona alohidami yoki yo'q
    @NotBlank
    @NotNull
    private String resource; // camera, telefon, televizor, compyuter. Nechtaligi, markasi va nomi
    @NotBlank
    @NotNull
    private String businessTrip; // Xizmat safari, Adress.
    @NotBlank
    @NotNull
    private String skills; // kasbiy va qo'shimcha qobiliyatlari
}
