package uz.otfiv.universitymediamonitoringsystem.service.interfaces;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.MediaProjectDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.MediaProject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface MediaProjectService {
    ResponseEntity<?> create(@Valid MediaProjectDTO mediaProjectDTO);

    ResponseEntity<?> delete(UUID id);

    ResponseEntity<?> getAll();

    List<MediaProject> findByEmployeeId(UUID employeeId);

    List<MediaProject> findDeletedByEmployeeId(UUID id);

    Optional<MediaProject> findById(UUID postId);

    void absoluteDeleted(UUID id);

    ResponseEntity<?> recovered(UUID mediaProjectId);

    List<MediaProject> findDeletedByAdminId(UUID adminId);

    List<MediaProject> findAdminMediaProjects(UUID adminId);

    List<MediaProject> findAllMediaEvents();
}
