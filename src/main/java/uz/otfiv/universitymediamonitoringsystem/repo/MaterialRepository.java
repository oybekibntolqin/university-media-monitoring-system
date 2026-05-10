package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.otfiv.universitymediamonitoringsystem.entity.Material;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.MaterialType;
import uz.otfiv.universitymediamonitoringsystem.projection.GetAllMaterialsProjection;
import uz.otfiv.universitymediamonitoringsystem.projection.GetMaterialCountProjection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaterialRepository extends JpaRepository<Material, UUID> {
    @Query(value = "select m.* from material m where m.employee_id = ?1 and m.material_type = ?2 and m.is_deleted = false", nativeQuery = true)
    List<Material> findAllByEmployeeIdAndType(UUID employeeId, MaterialType materialType);

    List<Material> findAllByEmployeeIdAndIsDeletedFalse(UUID employeeId);

    List<Material> findAllByAdminIdAndIsDeletedFalse(UUID adminId);

    List<Material> findMaterialsByEmployeeIdAndIsDeletedTrue(UUID employeeId);

    List<Material> findMaterialsByEmployeeIdAndIsDeletedFalse(UUID id);

    List<Material> findMaterialsByAdminIdAndIsDeletedFalse(UUID id);

    Optional<Material> findMaterialsByIdAndIsDeletedFalse(UUID materialId);

    List<Material> findMaterialsByAdminIdAndIsDeletedTrue(UUID adminId);

    List<Material> findAllByIsDeletedFalse();

    @Query(value = """
            select m.id as id, m.topic, m.publish_date, m.material_type, m.mass_media, m.social_media_name, m.link, m.grade
            from material m where m.is_deleted = false and m.employee_id = ?1
            """, nativeQuery = true)
    List<GetAllMaterialsProjection> findAllByEmployeeId(UUID employeeId);

    @Query(value = """
             SELECT
                COUNT(me) AS materialCount,
                SUM(CASE WHEN me.material_type = 'INFOGRAPHIC' THEN 1 ELSE 0 END) AS InfographicCount,
                SUM(CASE WHEN me.material_type = 'AUDIO' THEN 1 ELSE 0 END) AS AudioCount,
                SUM(CASE WHEN me.material_type = 'VIDEO' THEN 1 ELSE 0 END) AS VideoCount
            FROM material me where me.employee_id is not null and is_deleted = false
            """, nativeQuery = true)
    List<GetMaterialCountProjection> getMaterialCount();
}