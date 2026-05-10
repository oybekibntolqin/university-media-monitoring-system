package uz.otfiv.universitymediamonitoringsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.entity.Role;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.RoleName;
import uz.otfiv.universitymediamonitoringsystem.repo.RoleRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.RoleService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role findRoleNameByName(RoleName roleName) {
        return roleRepository.findRoleByRoleName(roleName);
    }

    @Override
    public void saveAll(List<Role> roleAdmin) {
        roleRepository.saveAll(roleAdmin);
    }
}
