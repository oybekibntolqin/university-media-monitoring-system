package uz.otfiv.universitymediamonitoringsystem.service.interfaces;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.MaterialDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Material;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.MaterialType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface MaterialService {
    ResponseEntity<?> create(MaterialDTO materialDTO);

    ResponseEntity<?> delete(UUID materialId);

    ResponseEntity<?> update(Material material);

    ResponseEntity<?> getMaterials();

    ResponseEntity<?> getAll(MaterialType materialType);

    List<Material> findDeletedMaterialsByEmployeeId(UUID id);

    ResponseEntity<?> findAll();

    Optional<Material> findById(UUID materialId);

    List<Material> findByEmployeeId(UUID employeeId);

    ResponseEntity<?> absoluteDeleted(UUID id);

    ResponseEntity<?> recovered(UUID materialId);

    List<Material> findDeletedMaterialsByAdminId(UUID id);

    List<Material> findAdminMaterials(UUID adminId);

    List<Material> findAllMaterials();

    ResponseEntity<?> getMaterialCount();
}
