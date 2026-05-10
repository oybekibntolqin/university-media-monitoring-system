package uz.otfiv.universitymediamonitoringsystem.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.entity.PasswordResetToken;
import uz.otfiv.universitymediamonitoringsystem.entity.User;

@Service
public interface PasswordResetTokenService {

    ResponseEntity<PasswordResetToken> createPasswordResetTokenForUser(User user, String token);

    boolean validatePasswordResetToken(String token);

    PasswordResetToken findByToken(String token);
}
