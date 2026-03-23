package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Messenger;

import java.math.BigInteger;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "official_page")
public class OfficialPage extends AbsEntity {
    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING) // Enumni string sifatida saqlash uchun
    @MapKeyColumn(name = "messenger")  // Map'ning key'ini ko'rsatish uchun
    @Column(name = "link")
    private Map<Messenger, String> messengersAndLinks;

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING) // Enumni string sifatida saqlash uchun
    @MapKeyColumn(name = "messenger")  // Map'ning key'ini ko'rsatish uchun
    @Column(name = "followers")
    private Map<Messenger, BigInteger> followers;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Admin admin;
}