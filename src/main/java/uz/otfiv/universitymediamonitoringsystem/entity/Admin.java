package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Admin extends AbsEntity {
    @OneToOne
    private  User user;
}
