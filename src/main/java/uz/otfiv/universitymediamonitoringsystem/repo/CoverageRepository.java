package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.otfiv.universitymediamonitoringsystem.entity.Coverage;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface CoverageRepository extends JpaRepository<Coverage, UUID> {

    List<Coverage> findCoveragesByEmployeeAndIsDeletedFalse(Employee employee);

    List<Coverage> findCoveragesByEmployeeIdAndIsDeletedFalse(UUID employeeId);

    List<Coverage> findCoveragesByEmployeeIdAndIsDeletedTrue(UUID id);

    List<Coverage> findCoveragesByAdminIdAndIsDeletedFalse(UUID id);

    List<Coverage> findCoveragesByAdminIdAndIsDeletedTrue(UUID adminId);

    List<Coverage> findAllByIsDeletedFalse();
}