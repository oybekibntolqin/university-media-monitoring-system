package uz.otfiv.universitymediamonitoringsystem.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.otfiv.universitymediamonitoringsystem.dto.OrganizationDTO;
import uz.otfiv.universitymediamonitoringsystem.dto.UserDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Details;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.exception.GlobalExceptionHandler;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.EmployeeService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.OrganizationService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.UserService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final GlobalExceptionHandler globalExceptionHandler;
    private final OrganizationService organizationService;
    private final UserService userService;


    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/create-organization")
    public ResponseEntity<?> fillOrganization(@RequestBody OrganizationDTO organizationDTO) {
        return organizationService.fillOrganization(organizationDTO);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/id")
    public ResponseEntity<?> getEmployeeById() {
        return userService.getEmployeeId();
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/average-salary-upload")
    public ResponseEntity<?> fillAverageSalaryUpload(@RequestParam("averageSalaryFile") MultipartFile file) {
        return employeeService.uploadSalaryFile(file);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/download-salary")
    public ResponseEntity<byte[]> downloadSalary(@RequestParam UUID userId) {
        try {
            Optional<Employee> optionalEmployee = employeeService.findByUserId(userId);

            if (optionalEmployee.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Employee employee = optionalEmployee.get();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(employee.getAttachmentContent().getContentType()));
            headers.setContentDispositionFormData("attachment", "xodimlarning o'rtacha maoshi." + employee.getAttachmentContent().getContentType());

            return new ResponseEntity<>(employee.getAttachmentContent().getContent(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PreAuthorize(value = "hasRole('EMPLOYEE')")
    @PostMapping("/settings/upload-photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam MultipartFile photo) {
        try {
            String email = userService.getEmail();
            if (email.isBlank() || email.equals("anonymousUser")) {
                return ResponseEntity.badRequest().body("user not found");
            }

            ResponseEntity<?> newEmployee = employeeService.getEmployeeUpdatePhoto(photo, email);
            if (newEmployee != null) return newEmployee;
            return ResponseEntity.badRequest().body("Employee not found");
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize(value = "hasRole('EMPLOYEE')")
    @PostMapping("/settings/fill-details")
    public ResponseEntity<?> fillDetails(@Valid @RequestBody UserDTO userDTO) {
        return employeeService.addUserDto(userDTO);
    }

    @PreAuthorize(value = "hasRole('EMPLOYEE')")
    @PutMapping("/settings/edit-details")
    public ResponseEntity<?> editDetails(@RequestBody Details details) {
        return employeeService.updateDetails(details);
    }

    @PreAuthorize(value = "hasRole('EMPLOYEE')")
    @GetMapping("/available-details")
    public ResponseEntity<?> getAvailableDetails() {
        try {
            return employeeService.isAvailableDetails();
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
