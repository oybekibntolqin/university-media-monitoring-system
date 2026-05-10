package uz.otfiv.universitymediamonitoringsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.otfiv.universitymediamonitoringsystem.dto.EmployeeDTO;
import uz.otfiv.universitymediamonitoringsystem.dto.NotificationDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.Notification;
import uz.otfiv.universitymediamonitoringsystem.projection.EmployeeWithOrganizationDTO;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final EmployeeService employeeService;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final OrganizationService organizationService;

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/get-organization")
    public ResponseEntity<?> getOrganization() {
        return ResponseEntity.ok(organizationService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all-employees")
    public ResponseEntity<?> getAllEmployees() {
        List<EmployeeWithOrganizationDTO> users = userService.getEmployeeWithOrganization();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-employee-details")
    public ResponseEntity<?> getEmployeeDetails(@RequestParam UUID userId) {
        Optional<Employee> optionalEmployee = employeeService.findByUserId(userId);

        if (optionalEmployee.isPresent()) {
            return ResponseEntity.ok(optionalEmployee.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-employee")
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return emailService.sendEmail(employeeDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete-employee-post")
    public ResponseEntity<?> deleteEmployeePost(@RequestBody NotificationDTO notificationDTO) {
        if (notificationDTO.getPostId() == null) {
            return ResponseEntity.badRequest().build();
        }
        ResponseEntity<?> resp = notificationService.createNotification(notificationDTO);

        Notification notification = (Notification) resp.getBody();

        assert notification != null;
        messagingTemplate.convertAndSend("/topic/notification/" + notification.getEmployee().getId(), notification);

        return resp;
    }
}
