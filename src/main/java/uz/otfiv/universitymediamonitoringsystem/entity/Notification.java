package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification extends AbsEntity {
    @OneToOne
    private Message message;
    @ManyToOne
    private Employee employee;//qaysi Employeega borishi Postni authori orqali topiladi
    private Boolean isRead = false;
    @ManyToOne
    private Admin admin;

}
