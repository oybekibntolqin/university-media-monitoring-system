package uz.otfiv.universitymediamonitoringsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.entity.Admin;
import uz.otfiv.universitymediamonitoringsystem.repo.AdminRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.AdminService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    @Override
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findAdminByUserEmail(email);
    }

    @Override
    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

}
