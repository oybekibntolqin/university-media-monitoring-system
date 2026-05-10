package uz.otfiv.universitymediamonitoringsystem.service.interfaces;

import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.entity.Admin;

import java.util.Optional;
import java.util.UUID;

@Service
public interface AdminService {
    Optional<Admin> findByEmail(String email);

    Admin save(Admin admin1);

}
