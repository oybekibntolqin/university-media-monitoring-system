package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.otfiv.universitymediamonitoringsystem.entity.ForeignMaterials;

import java.util.List;
import java.util.UUID;

public interface ForeignMaterialsRepository extends JpaRepository<ForeignMaterials, UUID> {
    List<ForeignMaterials> findForeignMaterialsByEmployeeIdAndIsDeletedFalse(UUID employeeId);

    List<ForeignMaterials> findForeignMaterialsByAdminIdAndIsDeletedFalse(UUID id);

    List<ForeignMaterials> findForeignMaterialsByEmployeeIdAndIsDeletedTrue(UUID employeeId);

    List<ForeignMaterials> findForeignMaterialsByAdminIdAndIsDeletedTrue(UUID adminId);

    List<ForeignMaterials> findAllByIsDeletedFalse();

    Integer countForeignMaterialsByIsDeletedFalseAndEmployeeIdIsNotNull();
}