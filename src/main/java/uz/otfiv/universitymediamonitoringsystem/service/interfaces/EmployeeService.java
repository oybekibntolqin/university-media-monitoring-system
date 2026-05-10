package uz.otfiv.universitymediamonitoringsystem.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.otfiv.universitymediamonitoringsystem.dto.OrganizationDTO;
import uz.otfiv.universitymediamonitoringsystem.dto.PasswordDTO;
import uz.otfiv.universitymediamonitoringsystem.dto.UserDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Details;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.Organization;
import uz.otfiv.universitymediamonitoringsystem.projection.EmployeeByGenderDTO;
import uz.otfiv.universitymediamonitoringsystem.projection.GenderProjection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface EmployeeService {

    Optional<Employee> findByUserId(UUID id);

    ResponseEntity<?> findByEmail(String email);

    Employee save(Employee employee);

    ResponseEntity<?> getEmployeeUpdatePhoto(MultipartFile photo, String email);

    void saveAll(List<Employee> employees);

    ResponseEntity<?> addUserDto(UserDTO userDTO);

    ResponseEntity<?> updateDetails(Details details);

    ResponseEntity<?> savePassword(PasswordDTO passwordDTO);

    boolean check(UUID employeeId);

    List<EmployeeByGenderDTO> getEmployeeByGender();

    Employee findById(UUID employeeId);

    List<Employee> findAll();

    List<GenderProjection> filterByProvince();

    ResponseEntity<?> getEmployeeInfo(UUID employeeId);

    ResponseEntity<?> getTop(int quantity);


    Organization updateOrganization(OrganizationDTO organizationDTO, Employee employee);

    ResponseEntity<?> isAvailableDetails();

    ResponseEntity<?> uploadSalaryFile(MultipartFile file);

    List<GenderProjection> getEmployeeByGenderRepublic();
}
