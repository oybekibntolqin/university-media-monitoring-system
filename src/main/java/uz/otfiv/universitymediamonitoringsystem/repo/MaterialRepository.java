package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.otfiv.universitymediamonitoringsystem.entity.Material;

import java.util.UUID;

public interface MaterialRepository extends JpaRepository<Material, UUID> {
}