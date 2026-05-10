package uz.otfiv.universitymediamonitoringsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.LoginDTO;
import uz.otfiv.universitymediamonitoringsystem.dto.TokenDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Admin;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.Role;
import uz.otfiv.universitymediamonitoringsystem.entity.User;
import uz.otfiv.universitymediamonitoringsystem.exception.GlobalExceptionHandler;
import uz.otfiv.universitymediamonitoringsystem.projection.EmployeeWithOrganizationDTO;
import uz.otfiv.universitymediamonitoringsystem.repo.EmployeeRepository;
import uz.otfiv.universitymediamonitoringsystem.repo.UserRepository;
import uz.otfiv.universitymediamonitoringsystem.security.JwtUtil;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.AdminService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.EmailService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.PasswordResetTokenService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final GlobalExceptionHandler globalExceptionHandler;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PasswordResetTokenService passwordResetTokenService;

    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCK_TIME_DURATION = 1; // in minutes


    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updated(User user) {
        return userRepository.save(user);
    }

    @Override
    public void increaseFailedAttempts(User user) {
        try {
            int failedAttempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(failedAttempts);

            if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                lock(user);
            }
            userRepository.save(user);
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
        }
    }

    @Override
    public void resetFailedAttempts(User user) {
        user.setFailedAttempts(0);
        user.setLockTime(null);
        updated(user);
    }

    @Override
    public void lock(User user) {
        user.setLockTime(LocalDateTime.now());
    }

    @Override
    public boolean isUserLocked(User user) {
        try {
            if (user.getLockTime() == null) {
                return false;
            }
            long lockTimeInMinutes = ChronoUnit.MINUTES.between(user.getLockTime(), LocalDateTime.now());

            if (lockTimeInMinutes >= LOCK_TIME_DURATION) {
                resetFailedAttempts(user);
                return false;
            }
            return true;
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return false;
        }
    }

    @Override
    public boolean passwordIsValid(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public List<EmployeeWithOrganizationDTO> getEmployeeWithOrganization() {
        return userRepository.getEmployeeWithOrganization();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public ResponseEntity<?> login(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        Optional<User> byEmail = findByEmail(email);

        if (byEmail.isEmpty() || email.isBlank() || email.equals("anonymousUser")) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = byEmail.get();

        if (isUserLocked(user)) {
            return ResponseEntity.badRequest().body("User is locked. Please try again later");
        }

        if (passwordIsValid(user, password)) {
            resetFailedAttempts(user);
            var aut = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authenticate = authenticationManager.authenticate(aut);
            UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

            @SuppressWarnings("unchecked")
            List<Role> roles = (List<Role>) authenticate.getAuthorities();

            TokenDTO body = new TokenDTO("Bearer " + jwtUtil.generateToken(userDetails), roles);
            return ResponseEntity.ok(body);
        } else {
            increaseFailedAttempts(user);
            return ResponseEntity.badRequest().body("Incorrect password");
        }
    }

    @Override
    public ResponseEntity<?> getMe() {
        String email = getEmail();
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByUserEmailAndIsDeletedFalse(email);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            return ResponseEntity.ok(employee);
        } else {
            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                return ResponseEntity.ok(optionalAdmin.get());
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public ResponseEntity<?> forgotPassword(String email) {
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByUserEmailAndIsDeletedFalse(email);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            String token = UUID.randomUUID().toString();
            passwordResetTokenService.createPasswordResetTokenForUser(employee.getUser(), token);
            emailService.sendResetTokenEmail(employee.getUser().getEmail(), token);
            return ResponseEntity.ok(token);
        } else {
            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();
                String token = UUID.randomUUID().toString();
                passwordResetTokenService.createPasswordResetTokenForUser(admin.getUser(), token);
                emailService.sendResetTokenEmail(admin.getUser().getEmail(), token);
                return ResponseEntity.ok(token);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> getEmployeeId() {
        String email = getEmail();
        Optional<User> optionalUser = findByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = optionalUser.get();
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByUserIdAndIsDeletedFalse(user.getId());
        if (optionalEmployee.isPresent()) {
            return ResponseEntity.ok(optionalEmployee.get().getId());
        }
        return ResponseEntity.notFound().build();
    }
}
