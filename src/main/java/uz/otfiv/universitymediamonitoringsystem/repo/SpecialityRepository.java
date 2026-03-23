package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.otfiv.universitymediamonitoringsystem.entity.Speciality;

import java.util.UUID;

public interface SpecialityRepository extends JpaRepository<Speciality, UUID> {
}