package uz.otfiv.universitymediamonitoringsystem.service;

import uz.otfiv.universitymediamonitoringsystem.dto.MaterialDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Admin;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.Material;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.MaterialType;
import uz.otfiv.universitymediamonitoringsystem.repo.MaterialRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final EmployeeService employeeService;
    private final AdminService adminService;
    private final UserService userService;


    @Override
    public ResponseEntity<?> create(MaterialDTO materialDTO) {

        String email = userService.getEmail();
        ResponseEntity<?> resp = employeeService.findByEmail(email);

        System.out.println("email = " + email);

        Material material = new Material();
        material.setGrade(Grade.GRADE_FIVE);

        if (resp.getStatusCode().value() == 200) {
            var employee = (Employee) resp.getBody();

            material.setEmployee(employee);
            assert employee != null;
            if (employee.getRating() != null) {
                employee.setRating(employee.getRating() + material.getGrade().getValue());
            } else {
                employee.setRating(material.getGrade().getValue());
            }
            employeeService.save(employee);
        } else {
            var optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();
                material.setAdmin(admin);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        material.setLink(materialDTO.getLink());
        material.setTopic(materialDTO.getTopic());
        material.setMaterialType((MaterialType.fromValue(materialDTO.getMaterialType())));
        material.setPublishDate(materialDTO.getPublishDate());
        material.setSocialMediaName(materialDTO.getSocialMediaName());
        material.setMassMedia(materialDTO.getMassMedia());

        materialRepository.save(material);
        return ResponseEntity.status(201).body(material);
    }

    @Override
    public ResponseEntity<?> delete(UUID materialId) {

        Optional<Material> optionalMaterial = findById(materialId);
        if (optionalMaterial.isPresent()) {

            optionalMaterial.get().setIsDeleted(true);
            materialRepository.save(optionalMaterial.get());
            return ResponseEntity.status(HttpStatus.OK).body("successfully deleted");
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> update(Material material) {
        return ResponseEntity.status(HttpStatus.OK).body(materialRepository.save(material));
    }

    @Override
    public ResponseEntity<?> getMaterials() {
        String email = userService.getEmail();
        ResponseEntity<?> byEmail = employeeService.findByEmail(email);

        List<Material> materials = new ArrayList<>();

        if (byEmail.getStatusCode() == HttpStatus.OK) {
            Employee employee = (Employee) byEmail.getBody();
            if (employee != null) {
                materials = materialRepository.findAllByEmployeeIdAndIsDeletedFalse(employee.getId());
            }
        } else {
            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();
                materials = materialRepository.findAllByAdminIdAndIsDeletedFalse(admin.getId());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("admin not found");
            }
        }

        if (materials.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.status(HttpStatus.OK).body(materials);
    }

    @Override
    public ResponseEntity<?> getAll(MaterialType materialType) {
        String email = userService.getEmail();
        ResponseEntity<?> resp = employeeService.findByEmail(email);

        if (resp.getStatusCode().value() != 200) {
            return ResponseEntity.notFound().build();
        }
        Employee employee = (Employee) resp.getBody();

        assert employee != null;
        List<Material> materials = materialRepository.findAllByEmployeeIdAndType(employee.getId(), materialType);
        return ResponseEntity.ok().body(materials);
    }

    @Override
    public List<Material> findDeletedMaterialsByEmployeeId(UUID employeeId) {
        return materialRepository.findMaterialsByEmployeeIdAndIsDeletedTrue(employeeId);
    }

    @Override
    public ResponseEntity<?> findAll() {
        String email = userService.getEmail();
        ResponseEntity<?> byEmail = employeeService.findByEmail(email);

        if (byEmail.getStatusCode() == HttpStatus.NOT_FOUND) {
            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("admin not found");
            }
            Admin admin = optionalAdmin.get();
            return ResponseEntity.ok(materialRepository.findMaterialsByAdminIdAndIsDeletedFalse(admin.getId()));
        } else {
            Employee employee = (Employee) byEmail.getBody();
            assert employee != null;
            return ResponseEntity.ok(materialRepository.findAllByEmployeeId(employee.getId()));
        }
    }

    @Override
    public Optional<Material> findById(UUID materialId) {
        return materialRepository.findMaterialsByIdAndIsDeletedFalse(materialId);
    }

    @Override
    public List<Material> findByEmployeeId(UUID employeeId) {
        return materialRepository.findMaterialsByEmployeeIdAndIsDeletedFalse(employeeId);
    }

    @Override
    public ResponseEntity<?> absoluteDeleted(UUID id) {

        if (id == null) {
            return ResponseEntity.badRequest().body("id is null");
        }

        materialRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("successfully deleted");
    }

    @Override
    public ResponseEntity<?> recovered(UUID materialId) {
        Optional<Material> optionalMaterial = findByIdIsDeleted(materialId);

        if (optionalMaterial.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Material not found");
        }
        optionalMaterial.get().setIsDeleted(false);
        materialRepository.save(optionalMaterial.get());
        return ResponseEntity.status(HttpStatus.OK).body("successfully recovered");
    }

    @Override
    public List<Material> findDeletedMaterialsByAdminId(UUID adminId) {
        return materialRepository.findMaterialsByAdminIdAndIsDeletedTrue(adminId);
    }

    @Override
    public List<Material> findAdminMaterials(UUID adminId) {
        return materialRepository.findAllByAdminIdAndIsDeletedFalse(adminId);
    }

    @Override
    public List<Material> findAllMaterials() {
        return materialRepository.findAllByIsDeletedFalse();
    }

    @Override
    public ResponseEntity<?> getMaterialCount() {
        return ResponseEntity.ok(materialRepository.getMaterialCount());
    }

    private Optional<Material> findByIdIsDeleted(UUID materialId) {
        return materialRepository.findById(materialId);
    }

}
