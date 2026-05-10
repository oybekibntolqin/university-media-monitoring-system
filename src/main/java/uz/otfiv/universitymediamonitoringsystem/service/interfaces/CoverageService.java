package uz.otfiv.universitymediamonitoringsystem.service.interfaces;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.CoverageDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Coverage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface CoverageService {
    ResponseEntity<?> create(@Valid CoverageDTO coverageDTO);

    ResponseEntity<?> delete(UUID id);

    ResponseEntity<?> getAll();

    List<Coverage> findByEmployeeId(UUID employeeId);

    List<Coverage> findDeletedByEmployeeId(UUID id);

    Optional<Coverage> findById(UUID postId);

    void absoluteDeleted(UUID id);

    ResponseEntity<?> recovered(UUID coverageId);

    List<Coverage> findDeletedByAdminId(UUID id);

    List<Coverage> findAdminCoverages(UUID adminId);

    List<Coverage> findAllCoverages();

}
