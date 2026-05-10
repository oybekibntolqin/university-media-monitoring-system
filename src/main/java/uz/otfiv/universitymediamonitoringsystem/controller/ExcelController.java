package uz.otfiv.universitymediamonitoringsystem.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.otfiv.universitymediamonitoringsystem.entity.Admin;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.service.ExcelService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.AdminService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.EmployeeService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.PostService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.UserService;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {
    private final ExcelService excelService;
    private final PostService postService;
    private final EmployeeService employeeService;
    private final AdminService adminService;
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/export-employees")
    public ResponseEntity<byte[]> downloadEmployeesExcel() {
        try {
            byte[] excelData = excelService.exportEmployeesExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "employees.xlsx");

            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/export-employee-posts")
    public ResponseEntity<?> getAllPostsAsExcel(@RequestParam UUID employeeId) {
        return postService.exportExcelPosts(employeeId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/export-admin-posts")
    public ResponseEntity<?> getAllAdminPosts() {
        String email = userService.getEmail();
        Optional<Admin> optionalAdmin = adminService.findByEmail(email);
        System.out.println("optionalAdmin = " + optionalAdmin);
        System.out.println("email in /export-admin-posts= " + email);
        if (optionalAdmin.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin not found");
        }
        return postService.exportExcelAdminPosts(optionalAdmin.get());
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/export-employee-materials")
    public ResponseEntity<?> export(@RequestParam UUID employeeId) {
        Employee employee = employeeService.findById(employeeId);

        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
        try {

            byte[] excelData = excelService.exportEmployeeMaterials(employee);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "employee-materials.xlsx");
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/export-employee-broadcasts")
    public ResponseEntity<?> exportEmployeeBroadcasts(@RequestParam UUID employeeId) {
        Employee employee = employeeService.findById(employeeId);

        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
        try {

            byte[] excelData = excelService.exportEmployeeBroadcasts(employee);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "employee-broadcasts.xlsx");
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/export-employee-media-events")
    public ResponseEntity<?> exportEmployeeMediaEvents(@RequestParam UUID employeeId) {
        Employee employee = employeeService.findById(employeeId);

        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
        try {

            byte[] excelData = excelService.exportEmployeeMediaEvents(employee);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "employee-media-events.xlsx");
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/export-employee-media-projects")
    public ResponseEntity<?> exportMediaProjects(@RequestParam UUID employeeId) {
        Employee employee = employeeService.findById(employeeId);

        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
        try {

            byte[] excelData = excelService.exportEmployeeMediaProjects(employee);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "employee-media-projects.xlsx");
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/export-employee-coverages")
    public ResponseEntity<?> exportEmployeeCoverages(@RequestParam UUID employeeId) {
        Employee employee = employeeService.findById(employeeId);

        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
        try {

            byte[] excelData = excelService.exportEmployeeCoverages(employee);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "employee-coverages.xlsx");
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/export-employee-foreign-material")
    public ResponseEntity<?> exportEmployeeForeignMaterial(@RequestParam UUID employeeId) {
        Employee employee = employeeService.findById(employeeId);

        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
        try {

            byte[] excelData = excelService.exportEmployeeForeignMaterials(employee);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "employee-coverages.xlsx");
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
