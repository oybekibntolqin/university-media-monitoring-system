package uz.otfiv.universitymediamonitoringsystem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import uz.otfiv.universitymediamonitoringsystem.dto.EmployeeDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.Role;
import uz.otfiv.universitymediamonitoringsystem.entity.User;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.RoleName;
import uz.otfiv.universitymediamonitoringsystem.exception.GlobalExceptionHandler;
import uz.otfiv.universitymediamonitoringsystem.repo.EmployeeRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.EmailService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.RoleService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final Random random = new Random();
    private final GlobalExceptionHandler globalExceptionHandler;
    private final EmployeeRepository employeeRepository;
    private final UserService userService;
    private final RoleService roleService;

    @Value("${url}")
    private String URL;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine, GlobalExceptionHandler globalExceptionHandler, EmployeeRepository employeeRepository, @Autowired
    @Lazy UserService userService, RoleService roleService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.globalExceptionHandler = globalExceptionHandler;
        this.employeeRepository = employeeRepository;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public ResponseEntity<?> sendEmail(EmployeeDTO employeeDTO) {

        Optional<User> optionalUser = userService.findByEmail(employeeDTO.getEmail());
        if (optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body("Bunday hodim allaqachon mavjud");
        }

        Role roleNameByName = roleService.findRoleNameByName(RoleName.ROLE_EMPLOYEE);
        User user = User.builder()
                .email(employeeDTO.getEmail())
                .password(employeeDTO.getPassword())
                .roles(List.of(roleNameByName))
                .build();

        userService.save(user);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("press@edu.uz");
            helper.setTo(employeeDTO.getEmail());
            helper.setSubject("Login ma'lumotlaringiz");


            Employee employee = Employee.builder()
                    .user(user)
                    .build();
            Employee saved = employeeRepository.save(employee);

            Context context = new Context();
//            context.setVariable("uuid", saved.getId());
            context.setVariable("email", user.getEmail());
            context.setVariable("password", employeeDTO.getPassword());

            String htmlContent = templateEngine.process("LinkSending", context);

            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Override
    public int generateRandomPassword() {
        return 100000 + random.nextInt(900000);
    }

    @Override
    public ResponseEntity<?> sendResetTokenEmail(String email, String token) {
        try {
            String url = URL + token;

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("Password Reset Request");
            mailMessage.setText("To reset your password, click the link below:\n" + url);
            mailSender.send(mailMessage);
        } catch (MailException e) {
            globalExceptionHandler.sendExceptionMessage(e);
        }
        return ResponseEntity.notFound().build();
    }
}
