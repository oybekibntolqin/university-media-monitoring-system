package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.otfiv.universitymediamonitoringsystem.entity.User;
import uz.otfiv.universitymediamonitoringsystem.projection.EmployeeWithOrganizationDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    @Query(value = """
            select  e.id as employee_id,u.id AS id,
                    u.full_name AS fullName,
                    o.name AS organizationName,
                    u.phone AS phone,
                    u.email AS email
            from users u inner join organization o on o.id = u.organization_id inner join employee e on u.id = e.user_id
            and e.is_deleted = false
            """,nativeQuery = true)
    List<EmployeeWithOrganizationDTO> getEmployeeWithOrganization();
}