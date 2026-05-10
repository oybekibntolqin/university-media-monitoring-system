package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.MediaProject;

import java.util.List;
import java.util.UUID;

public interface MediaProjectRepository extends JpaRepository<MediaProject, UUID> {

    List<MediaProject> findMediaProjectsByEmployeeAndIsDeletedFalse(Employee employee);

    List<MediaProject> findMediaProjectsByEmployeeIdAndIsDeletedFalse(UUID employeeId);

    List<MediaProject> findMediaProjectsByEmployeeIdAndIsDeletedTrue(UUID id);

    List<MediaProject> findMediaProjectsByAdminIdAndIsDeletedFalse(UUID id);

    List<MediaProject> findMediaProjectsByAdminIdAndIsDeletedTrue(UUID adminId);

    List<MediaProject> findAllByIsDeletedFalse();
}