package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.RoleName;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role extends AbsEntity implements GrantedAuthority {
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return this.roleName.name();
    }
}
