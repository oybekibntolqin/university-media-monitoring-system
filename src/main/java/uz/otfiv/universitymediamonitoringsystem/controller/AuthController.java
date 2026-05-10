package uz.otfiv.universitymediamonitoringsystem.controller;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.otfiv.universitymediamonitoringsystem.dto.ForgetPasswordDTO;
import uz.otfiv.universitymediamonitoringsystem.dto.LoginDTO;
import uz.otfiv.universitymediamonitoringsystem.dto.PasswordDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.PasswordResetToken;
import uz.otfiv.universitymediamonitoringsystem.entity.User;
import uz.otfiv.universitymediamonitoringsystem.exception.GlobalExceptionHandler;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.EmployeeService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.PasswordResetTokenService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final GlobalExceptionHandler globalExceptionHandler;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {

        sendEmail();
        try {
            return userService.login(loginDTO);
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public void sendEmail() {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("press@edu.uz"); // TO'G'RI jo'natuvchi manzili
            helper.setTo("shukurullayevjavoxir777@gmail.com"); // Qabul qiluvchi manzil
            helper.setSubject("Test Email");
            helper.setText("This is a test email");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


        javaMailSender.send(message);
    }


    @PostMapping("/savePassword")
    public ResponseEntity<?> savePassword(@RequestBody PasswordDTO passwordDTO) {
        return employeeService.savePassword(passwordDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        try {
            return userService.getMe();
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            return userService.forgotPassword(email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        String token = forgetPasswordDTO.getToken();
        String newPassword = forgetPasswordDTO.getNewPassword();
        String rePassword = forgetPasswordDTO.getRePassword();

        if (!rePassword.equals(newPassword)) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        try {
            boolean isValid = passwordResetTokenService.validatePasswordResetToken(token);

            if (!isValid) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
            }

            PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);
            User user = passwordResetToken.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.resetFailedAttempts(user);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
