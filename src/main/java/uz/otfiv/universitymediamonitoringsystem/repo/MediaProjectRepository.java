package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.otfiv.universitymediamonitoringsystem.entity.MediaProject;

import java.util.UUID;

public interface MediaProjectRepository extends JpaRepository<MediaProject, UUID> {
}