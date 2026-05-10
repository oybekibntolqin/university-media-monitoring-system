package uz.otfiv.universitymediamonitoringsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.entity.PasswordResetToken;
import uz.otfiv.universitymediamonitoringsystem.entity.User;
import uz.otfiv.universitymediamonitoringsystem.exception.GlobalExceptionHandler;
import uz.otfiv.universitymediamonitoringsystem.repo.PasswordResetTokenRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.PasswordResetTokenService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final GlobalExceptionHandler globalExceptionHandler;


    @Override
    public ResponseEntity<PasswordResetToken> createPasswordResetTokenForUser(User user, String token) {
        try {
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setUser(user);
            passwordResetToken.setToken(token);
            passwordResetToken.setExpiryTime(LocalDateTime.now().plusHours(1));
//            passwordResetToken.setExpiryTime(LocalDateTime.now().plusMinutes(5));
            passwordResetTokenRepository.save(passwordResetToken);
            return ResponseEntity.ok(passwordResetToken);
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token).orElse(null);

        return passToken != null && passToken.getExpiryTime().isAfter(LocalDateTime.now());
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token).orElse(null);
    }
}
