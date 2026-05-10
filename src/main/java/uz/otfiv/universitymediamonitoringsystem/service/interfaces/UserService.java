package uz.otfiv.universitymediamonitoringsystem.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.LoginDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.User;
import uz.otfiv.universitymediamonitoringsystem.projection.EmployeeWithOrganizationDTO;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    User save(User user);

    Optional<User> findByEmail(String email);

    User updated(User user);

    void increaseFailedAttempts(User user);

    void resetFailedAttempts(User user);

    void lock(User user);

    boolean isUserLocked(User user);


    boolean passwordIsValid(User user, String password);

    List<EmployeeWithOrganizationDTO> getEmployeeWithOrganization();

    List<User> findAll();

    ResponseEntity<?> login(LoginDTO loginDTO);

    ResponseEntity<?> getMe();

    String getEmail();

    ResponseEntity<?> forgotPassword(String email);

    ResponseEntity<?> getEmployeeId();
}
