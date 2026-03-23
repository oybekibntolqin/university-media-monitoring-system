package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.MapKeyColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
@Builder
public class Details {
    private String commendNumber; //buyruq raqami
    private LocalDate entryDate; // ishga kirgan vaqti
    private String license;  // AOKA Attestatsiyadan o'tganligi.Guvohnoma raqami
    private String qualificationInfo; // AOKA da malaka oshirish
    @ElementCollection
    @MapKeyColumn(name = "position")
    @Column(name = "type")
    private Map<String, String> workType; // shtatdagi o'rni
    private String averageSalary; // oxirgi 1 yillikdagi o'rtacha oylik
    private String advisor; //maslahatchi etib belgilanganmi yoki yo'qligi. Ha bo'lsa hujjat raqami
    private String departmentOrganisation; // Matubot bo'limi alohidami
    private String room; // Xona alohidami yoki yo'q
    private String resource; // camera, telefon, televizor, compyuter. Nechtaligi, markasi va nomi
    private String businessTrip; // Xizmat safari, Adress.
    private String skills; // kasbiy va qo'shimcha qobiliyatlari
}
