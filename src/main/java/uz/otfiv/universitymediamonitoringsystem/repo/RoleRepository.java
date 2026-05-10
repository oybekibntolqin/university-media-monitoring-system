package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.otfiv.universitymediamonitoringsystem.entity.Role;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.RoleName;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findRoleByRoleName(RoleName roleName);
}