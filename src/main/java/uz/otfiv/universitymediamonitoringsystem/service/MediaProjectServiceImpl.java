package uz.otfiv.universitymediamonitoringsystem.service;


import uz.otfiv.universitymediamonitoringsystem.dto.MediaProjectDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Admin;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.MediaProject;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Period;
import uz.otfiv.universitymediamonitoringsystem.repo.MediaProjectRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.AdminService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.EmployeeService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.MediaProjectService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaProjectServiceImpl implements MediaProjectService {
    private final MediaProjectRepository mediaProjectRepository;
    private final EmployeeService employeeService;
    private final AdminService adminService;
    private final UserService userService;


    @Override
    public ResponseEntity<?> create(MediaProjectDTO mediaProjectDTO) {

        String email = userService.getEmail();
        ResponseEntity<?> resp = employeeService.findByEmail(email);

        if (email.isBlank() || email.equals("anonymous")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Admin> optionalAdmin = adminService.findByEmail(email);

        MediaProject mediaProject = new MediaProject();
        mediaProject.setGrade(Grade.GRADE_FIVE);

        if (resp.getStatusCode().value() == 200) {
            Employee employee = (Employee) resp.getBody();
            assert employee != null;

            mediaProject.setEmployee(employee);

            if (employee.getRating() != null) {
                employee.setRating(employee.getRating() + mediaProject.getGrade().getValue());
            } else {
                employee.setRating(mediaProject.getGrade().getValue());
            }
            employeeService.save(employee);
        } else if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            mediaProject.setAdmin(admin);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        mediaProject.setName(mediaProjectDTO.getName());
        mediaProject.setDescription(mediaProjectDTO.getDescription());
        mediaProject.setPeriod(Period.get(mediaProjectDTO.getPeriod()));
        mediaProject.setMassMedia(mediaProjectDTO.getMassMedia());
        mediaProject.setLink(mediaProjectDTO.getLink());

        return ResponseEntity.status(HttpStatus.CREATED).body(mediaProjectRepository.save(mediaProject));
    }

    @Override
    public ResponseEntity<?> delete(UUID id) {
        Optional<MediaProject> optional = mediaProjectRepository.findById(id);
        if (optional.isPresent()) {
            MediaProject mediaProject = optional.get();
            mediaProject.setIsDeleted(true);
            mediaProjectRepository.save(mediaProject);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("successfully deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
        }
    }

    @Override
    public ResponseEntity<?> getAll() {

        String email = userService.getEmail();
        ResponseEntity<?> resp = employeeService.findByEmail(email);
        if (resp.getStatusCode() == HttpStatus.OK) {
            Employee employee = (Employee) resp.getBody();

            return ResponseEntity.ok(mediaProjectRepository.findMediaProjectsByEmployeeAndIsDeletedFalse(employee));
        } else {
            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();
                return ResponseEntity.ok(mediaProjectRepository.findMediaProjectsByAdminIdAndIsDeletedFalse(admin.getId()));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Override
    public List<MediaProject> findByEmployeeId(UUID employeeId) {
        return mediaProjectRepository.findMediaProjectsByEmployeeIdAndIsDeletedFalse(employeeId);
    }

    @Override
    public List<MediaProject> findDeletedByEmployeeId(UUID id) {
        return mediaProjectRepository.findMediaProjectsByEmployeeIdAndIsDeletedTrue(id);
    }

    @Override
    public Optional<MediaProject> findById(UUID postId) {
        return mediaProjectRepository.findById(postId);
    }

    @Override
    public void absoluteDeleted(UUID id) {
        mediaProjectRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<?> recovered(UUID mediaProjectId) {
        Optional<MediaProject> optionalMediaProject = findById(mediaProjectId);
        if (optionalMediaProject.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        optionalMediaProject.get().setIsDeleted(false);
        mediaProjectRepository.save(optionalMediaProject.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public List<MediaProject> findDeletedByAdminId(UUID adminId) {
        return mediaProjectRepository.findMediaProjectsByAdminIdAndIsDeletedTrue(adminId);
    }

    @Override
    public List<MediaProject> findAdminMediaProjects(UUID adminId) {
        return mediaProjectRepository.findMediaProjectsByAdminIdAndIsDeletedFalse(adminId);
    }

    @Override
    public List<MediaProject> findAllMediaEvents() {
        return mediaProjectRepository.findAllByIsDeletedFalse();
    }
}
