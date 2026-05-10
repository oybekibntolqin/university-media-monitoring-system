package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.OfficialPage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfficialPageRepository extends JpaRepository<OfficialPage, UUID> {

    List<OfficialPage> findAllByEmployee(Employee employee);


    List<OfficialPage> findOfficialPagesByEmployeeAndIsDeletedFalse(Employee employee);

    List<OfficialPage> findOfficialPagesByEmployeeIdAndIsDeletedFalse(UUID id);

    @Query(value = "select o.* from official_page o inner join official_page_messengers_and_links ofp on o.id = ofp.official_page_id where ofp.messenger = ?1 and o.employee_id = ?2 and o.is_deleted = false", nativeQuery = true)
    Optional<OfficialPage> findOfficialPageByMessengers(String messenger, UUID employeeId);

    List<OfficialPage> findOfficialPagesByEmployeeIdAndIsDeletedTrue(UUID employeeId);

    List<OfficialPage> findOfficialPagesByAdminIdAndIsDeletedFalse(UUID id);

    List<OfficialPage> findOfficialPagesByAdminIdAndIsDeletedTrue(UUID adminId);
}