package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.otfiv.universitymediamonitoringsystem.entity.ForeignMaterials;

import java.util.UUID;

public interface ForeignMaterialsRepository extends JpaRepository<ForeignMaterials, UUID> {
}