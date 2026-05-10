package uz.otfiv.universitymediamonitoringsystem.service.interfaces;

import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.entity.Role;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.RoleName;

import java.util.List;

@Service
public interface RoleService {

    Role findRoleNameByName(RoleName roleName);

    void saveAll(List<Role> roleAdmin);
}
