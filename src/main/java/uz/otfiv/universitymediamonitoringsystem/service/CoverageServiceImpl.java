package uz.otfiv.universitymediamonitoringsystem.service;


import uz.otfiv.universitymediamonitoringsystem.dto.CoverageDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Admin;
import uz.otfiv.universitymediamonitoringsystem.entity.Coverage;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;
import uz.otfiv.universitymediamonitoringsystem.repo.CoverageRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.AdminService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.CoverageService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.EmployeeService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoverageServiceImpl implements CoverageService {
    private final CoverageRepository coverageRepository;
    private final EmployeeService employeeService;
    private final AdminService adminService;
    private final UserService userService;

    @Override
    public ResponseEntity<?> create(CoverageDTO coverageDTO) {

        String email = userService.getEmail();

        ResponseEntity<?> resp = employeeService.findByEmail(email);
        Optional<Admin> optionalAdmin = adminService.findByEmail(email);

        Coverage coverage = new Coverage();
        coverage.setGrade(Grade.GRADE_FIVE);

        if (resp.getStatusCode().value() == 200) {
            Employee employee = getEmployee(resp);
            coverage.setEmployee(employee);
            if (employee.getRating() != null) {
                employee.setRating(employee.getRating() + coverage.getGrade().getValue());
            } else {
                employee.setRating(coverage.getGrade().getValue());
            }
            employeeService.save(employee);
        } else if (optionalAdmin.isPresent()) {
            coverage.setAdmin(optionalAdmin.get());
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }

        coverage.setEventName(coverageDTO.getEventName());
        coverage.setEventType(coverageDTO.getEventType());
        coverage.setMassMedia(coverageDTO.getMassMedia());
        coverage.setMediaLinks(coverageDTO.getMediaLinks());
        coverage.setPublishType(coverageDTO.getPublishType());
        coverage.setPublishDate(coverageDTO.getPublishedDate());
        Coverage save = coverageRepository.save(coverage);

        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    public ResponseEntity<?> delete(UUID id) {
        Optional<Coverage> optional = coverageRepository.findById(id);
        if (optional.isPresent()) {
            Coverage coverage = optional.get();
            coverage.setIsDeleted(true);
            coverageRepository.save(coverage);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("successfully deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
        }
    }

    @Override
    public ResponseEntity<?> getAll() {
        String email = userService.getEmail();

        ResponseEntity<?> resp = employeeService.findByEmail(email);

        if (resp.getStatusCode() == HttpStatus.OK) {
            Employee employee = (Employee) resp.getBody();
            return ResponseEntity.ok(coverageRepository.findCoveragesByEmployeeAndIsDeletedFalse(employee));
        } else {
            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();
                return ResponseEntity.ok(coverageRepository.findCoveragesByAdminIdAndIsDeletedFalse(admin.getId()));
            }
        }
        return ResponseEntity.notFound().build();

    }

    @Override
    public List<Coverage> findByEmployeeId(UUID employeeId) {
        return coverageRepository.findCoveragesByEmployeeIdAndIsDeletedFalse(employeeId);
    }

    @Override
    public List<Coverage> findDeletedByEmployeeId(UUID id) {
        return coverageRepository.findCoveragesByEmployeeIdAndIsDeletedTrue(id);
    }

    @Override
    public Optional<Coverage> findById(UUID postId) {
        return coverageRepository.findById(postId);
    }

    @Override
    public void absoluteDeleted(UUID id) {
        coverageRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<?> recovered(UUID coverageId) {
        Optional<Coverage> optionalCoverage = findById(coverageId);
        if (optionalCoverage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
        }

        optionalCoverage.get().setIsDeleted(false);
        coverageRepository.save(optionalCoverage.get());
        return ResponseEntity.ok(optionalCoverage.get());
    }

    @Override
    public List<Coverage> findDeletedByAdminId(UUID adminId) {
        return coverageRepository.findCoveragesByAdminIdAndIsDeletedTrue(adminId);
    }

    @NotNull
    private static Employee getEmployee(ResponseEntity<?> resp) {
        Employee employee = (Employee) resp.getBody();
        assert employee != null;
        return employee;
    }

    @Override
    public List<Coverage> findAdminCoverages(UUID adminId) {
        return coverageRepository.findCoveragesByAdminIdAndIsDeletedFalse(adminId);
    }

    @Override
    public List<Coverage> findAllCoverages() {
        return coverageRepository.findAllByIsDeletedFalse();
    }
}
