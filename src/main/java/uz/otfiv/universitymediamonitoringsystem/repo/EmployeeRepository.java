package uz.otfiv.universitymediamonitoringsystem.repo;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.Organization;
import uz.otfiv.universitymediamonitoringsystem.projection.EmployeeByGenderDTO;
import uz.otfiv.universitymediamonitoringsystem.projection.GenderProjection;
import uz.otfiv.universitymediamonitoringsystem.projection.TopRatingEmployeesProjection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findEmployeeByUserIdAndIsDeletedFalse(UUID userId);

    Optional<Employee> findEmployeeByUserEmailAndIsDeletedFalse(String email);

    @Query(value = "select * from employee_gender_summary", nativeQuery = true)
    List<EmployeeByGenderDTO> getEmployeeSummary();


    @Query(value = """
        SELECT o.province AS province,
               SUM(CASE WHEN e.gender = 'male' THEN 1 ELSE 0 END) AS male,
               SUM(CASE WHEN e.gender = 'female' THEN 1 ELSE 0 END) AS female
        FROM employee e
        JOIN users u ON e.user_id = u.id
        JOIN organization o ON u.organization_id = o.id
        GROUP BY o.province
        """, nativeQuery = true)
    List<GenderProjection> getGendersByProvince();

    @NotNull
    @Query(value = "select * from employee where is_deleted = false and id = ?1", nativeQuery = true)
    Optional<Employee> findById(@NotNull UUID employeeId);

    @Query(value = "select * from top_employees",nativeQuery = true)
    List<TopRatingEmployeesProjection> getEmployeesByRating(Integer quantity);


    @Query("SELECT u.organization FROM Employee e JOIN e.user u WHERE e.id = :employeeId")
    Organization findOrganizationByEmployeeId(@Param("employeeId") UUID employeeId);

    @Query(value = """
            SELECT 'ALL' AS province,
                       SUM(CASE WHEN e.gender = 'male' THEN 1 ELSE 0 END) AS male,
                       SUM(CASE WHEN e.gender = 'female' THEN 1 ELSE 0 END) AS female
                FROM employee e
                JOIN users u ON e.user_id = u.id
                JOIN organization o ON u.organization_id = o.id
            """, nativeQuery = true)
    List<GenderProjection> getRepublicGenders();
}