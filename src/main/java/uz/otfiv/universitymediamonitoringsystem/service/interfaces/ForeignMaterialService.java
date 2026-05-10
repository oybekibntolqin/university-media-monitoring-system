package uz.otfiv.universitymediamonitoringsystem.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.ForeignMaterialDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.ForeignMaterials;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ForeignMaterialService {
    ResponseEntity<?> getAll();

    ResponseEntity<?> create(ForeignMaterialDTO foreignMaterialDTO);

    ResponseEntity<?> deleted(UUID foreignMaterialId);

    ResponseEntity<?> recovered(UUID foreignMaterialId);

    ResponseEntity<?> absoluteDeleted(UUID foreignMaterialId);

    List<ForeignMaterials> findDeletedByEmployeeId(UUID id);

    List<ForeignMaterials> findDeletedByAdminId(UUID adminId);

    List<ForeignMaterials> findByEmployeeId(UUID employeeId);

    List<ForeignMaterials> findByAdminForeignMaterials(UUID adminId);

    List<ForeignMaterials> findAllForeignMaterials();

    Optional<ForeignMaterials> findById(UUID foreignMaterialId);

    ResponseEntity<?> getAllCount();
}
