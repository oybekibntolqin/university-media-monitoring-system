package uz.otfiv.universitymediamonitoringsystem.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.EmployeeDTO;

@Service
public interface EmailService {

    ResponseEntity<?> sendEmail(EmployeeDTO employeeDTO);

    int generateRandomPassword();

    ResponseEntity<?> sendResetTokenEmail(String email, String token);
}
