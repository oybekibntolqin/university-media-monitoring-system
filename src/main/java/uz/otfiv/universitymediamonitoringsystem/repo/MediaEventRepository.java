package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.otfiv.universitymediamonitoringsystem.entity.MediaEvent;

import java.util.UUID;

public interface MediaEventRepository extends JpaRepository<MediaEvent, UUID> {
}