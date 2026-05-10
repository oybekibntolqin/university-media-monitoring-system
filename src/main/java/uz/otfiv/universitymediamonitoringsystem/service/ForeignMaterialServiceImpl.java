package uz.otfiv.universitymediamonitoringsystem.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.ForeignMaterialDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Admin;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.ForeignMaterials;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;
import uz.otfiv.universitymediamonitoringsystem.repo.ForeignMaterialsRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.AdminService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.EmployeeService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.ForeignMaterialService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("foreignMaterialService")
@RequiredArgsConstructor
public class ForeignMaterialServiceImpl implements ForeignMaterialService {
    private final ForeignMaterialsRepository foreignMaterialsRepository;
    private final UserService userService;
    private final AdminService adminService;
    private final EmployeeService employeeService;

    @Override
    public ResponseEntity<?> getAll() {
        var email = userService.getEmail();
        var resp = employeeService.findByEmail(email);
        if (resp.getStatusCode().value() == 200) {
            Employee employee = (Employee) resp.getBody();
            assert employee != null;
            return ResponseEntity.ok(foreignMaterialsRepository.findForeignMaterialsByEmployeeIdAndIsDeletedFalse(employee.getId()));
        } else {
            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();
                return ResponseEntity.ok(foreignMaterialsRepository.findForeignMaterialsByAdminIdAndIsDeletedFalse(admin.getId()));
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<?> create(ForeignMaterialDTO foreignMaterialDTO) {
        String email = userService.getEmail();
        var resp = employeeService.findByEmail(email);

        ForeignMaterials foreignMaterials = new ForeignMaterials();
        foreignMaterials.setGrade(Grade.GRADE_FIVE);

        if (resp.getStatusCode().value() == 200) {
            var employee = (Employee) resp.getBody();
            foreignMaterials.setEmployee(employee);
            assert employee != null;
            if (employee.getRating() != null) {
                employee.setRating(employee.getRating() + foreignMaterials.getGrade().getValue());
            } else {
                employee.setRating(foreignMaterials.getGrade().getValue());
            }
            employeeService.save(employee);
        } else {
            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();
                foreignMaterials.setAdmin(admin);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }

        foreignMaterials.setTitle(foreignMaterialDTO.getTitle());
        foreignMaterials.setIllumination(foreignMaterialDTO.getIllumination());
        foreignMaterials.setTypeMediaSocial(foreignMaterialDTO.getTypeMediaSocial());
        foreignMaterials.setPublishedDate(foreignMaterialDTO.getPublishedDate());
        foreignMaterials.setMediaNameAndLink(foreignMaterialDTO.getMediaNameAndLink());

        System.out.println("foreignMaterials.getMediaNameAndLink() = " + foreignMaterials.getMediaNameAndLink());

        foreignMaterialsRepository.save(foreignMaterials);
        return ResponseEntity.status(201).body(foreignMaterials);
    }

    @Override
    public ResponseEntity<?> deleted(UUID foreignMaterialId) {
        Optional<ForeignMaterials> optionalForeignMaterials = foreignMaterialsRepository.findById(foreignMaterialId);
        if (optionalForeignMaterials.isPresent()) {
            optionalForeignMaterials.get().setIsDeleted(true);
            foreignMaterialsRepository.save(optionalForeignMaterials.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<?> recovered(UUID foreignMaterialId) {
        Optional<ForeignMaterials> optionalForeignMaterials = foreignMaterialsRepository.findById(foreignMaterialId);
        if (optionalForeignMaterials.isPresent()) {
            optionalForeignMaterials.get().setIsDeleted(false);
            foreignMaterialsRepository.save(optionalForeignMaterials.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<?> absoluteDeleted(UUID foreignMaterialId) {
        if (foreignMaterialId == null) {
            return ResponseEntity.badRequest().build();
        }
        foreignMaterialsRepository.deleteById(foreignMaterialId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public List<ForeignMaterials> findDeletedByEmployeeId(UUID employeeId) {
        return foreignMaterialsRepository.findForeignMaterialsByEmployeeIdAndIsDeletedTrue(employeeId);
    }

    @Override
    public List<ForeignMaterials> findDeletedByAdminId(UUID adminId) {
        return foreignMaterialsRepository.findForeignMaterialsByAdminIdAndIsDeletedTrue(adminId);
    }

    @Override
    public List<ForeignMaterials> findByEmployeeId(UUID employeeId) {
        return foreignMaterialsRepository.findForeignMaterialsByEmployeeIdAndIsDeletedFalse(employeeId);
    }

    @Override
    public List<ForeignMaterials> findByAdminForeignMaterials(UUID adminId) {
        return foreignMaterialsRepository.findForeignMaterialsByAdminIdAndIsDeletedFalse(adminId);
    }

    @Override
    public List<ForeignMaterials> findAllForeignMaterials() {
        return foreignMaterialsRepository.findAll();
    }

    @Override
    public Optional<ForeignMaterials> findById(UUID postId) {
        return foreignMaterialsRepository.findById(postId);
    }

    @Override
    public ResponseEntity<?> getAllCount() {
        return ResponseEntity.ok(foreignMaterialsRepository.countForeignMaterialsByIsDeletedFalseAndEmployeeIdIsNotNull());
    }
}
