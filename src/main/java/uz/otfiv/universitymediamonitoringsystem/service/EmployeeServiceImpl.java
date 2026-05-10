package uz.otfiv.universitymediamonitoringsystem.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.otfiv.universitymediamonitoringsystem.dto.EmployeeInfoDTO;
import uz.otfiv.universitymediamonitoringsystem.dto.OrganizationDTO;
import uz.otfiv.universitymediamonitoringsystem.dto.PasswordDTO;
import uz.otfiv.universitymediamonitoringsystem.dto.UserDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.*;
import uz.otfiv.universitymediamonitoringsystem.exception.GlobalExceptionHandler;
import uz.otfiv.universitymediamonitoringsystem.projection.EmployeeByGenderDTO;
import uz.otfiv.universitymediamonitoringsystem.projection.GenderProjection;
import uz.otfiv.universitymediamonitoringsystem.projection.TopRatingEmployeesProjection;
import uz.otfiv.universitymediamonitoringsystem.repo.EmployeeRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.*;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserService userService;
    private final GlobalExceptionHandler globalExceptionHandler;
    private final OrganizationService organizationService;
    private final SpecialityService specialityService;
    private final PasswordEncoder passwordEncoder;
    private final PostService postService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserService userService, GlobalExceptionHandler globalExceptionHandler, @Lazy OrganizationService organizationService, SpecialityService specialityService, PasswordEncoder passwordEncoder, @Lazy PostService postService) {
        this.employeeRepository = employeeRepository;
        this.userService = userService;
        this.globalExceptionHandler = globalExceptionHandler;
        this.organizationService = organizationService;
        this.specialityService = specialityService;
        this.passwordEncoder = passwordEncoder;
        this.postService = postService;
    }

    @Override
    public Optional<Employee> findByUserId(UUID id) {
        return employeeRepository.findEmployeeByUserIdAndIsDeletedFalse(id);
    }

    @Override
    public ResponseEntity<?> findByEmail(String email) {

        try {
            Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByUserEmailAndIsDeletedFalse(email);

            if (optionalEmployee.isPresent()) {
                return ResponseEntity.ok(optionalEmployee.get());
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.notFound().build();
        }

    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public ResponseEntity<?> getEmployeeUpdatePhoto(MultipartFile photo, String email) {
        try {
            ResponseEntity<?> resp = findByEmail(email);

            if (resp.getStatusCode().value() == 200) {
                Employee employee = (Employee) resp.getBody();
                assert employee != null;
                User user = employee.getUser();

                AttachmentContent

                        attachmentContent = AttachmentContent.builder()
                        .size(photo.getSize())
                        .contentType(photo.getContentType())
                        .content(photo.getInputStream().readAllBytes())
                        .filename(photo.getOriginalFilename())
                        .build();

                user.setAttachmentContent(attachmentContent);

                userService.updated(user);
                employee.setUser(user);
                Employee newEmployee = save(employee);

                return ResponseEntity.ok(newEmployee);
            } else {
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Override
    public void saveAll(List<Employee> employees) {
        employeeRepository.saveAll(employees);
    }

    @Override
    @NotNull
    public ResponseEntity<?> addUserDto(UserDTO userDTO) {
        try {
            String email = userService.getEmail();

            if (email.isBlank() || email.equals("anonymousUser")) {
                return ResponseEntity.badRequest().body("user not found");
            }

            ResponseEntity<?> resp = findByEmail(email);

            Speciality speciality = Speciality.builder()
                    .specialities(userDTO.getSpecialities())
                    .build();
            specialityService.save(speciality);

            if (resp.getStatusCode().value() == 200) {
                Employee employee = (Employee) resp.getBody();

                assert employee != null;
                User user = employee.getUser();

                Organization organization = organizationService.findById(userDTO.getOrganizationId());

                if (organization == null) {
                    return ResponseEntity.badRequest().body("Organization not found");
                }

                user.setBirthday(userDTO.getBirthday());
                user.setFullName(userDTO.getFullName());
                user.setPhone(userDTO.getPhone());
                user.setOrganization(organization);
                userService.updated(user);

                Details details = getDetails(userDTO);

                employee.setUser(user);
                employee.setDetails(details);
                employee.setSpeciality(speciality);
                employee.setGender(userDTO.getGender());
                employeeRepository.save(employee);

                return ResponseEntity.ok().body(employee);
            }
            return resp;
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    public Details getDetails(UserDTO userDTO) {
        try {
            return Details.builder()
                    .advisor(userDTO.getAdvisor())
                    .averageSalary(userDTO.getAverageSalary())
                    .businessTrip(userDTO.getBusinessTrip())
                    .room(userDTO.getRoom())
                    .commendNumber(userDTO.getCommendNumber())
                    .license(userDTO.getLicense())
                    .entryDate(userDTO.getEntryDate())
                    .skills(userDTO.getSkills())
                    .departmentOrganisation(userDTO.getDepartmentOrganisation())
                    .qualificationInfo(userDTO.getQualificationInfo())
                    .resource(userDTO.getResource())
                    .workType(userDTO.getWorkType())
                    .build();
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return null;
        }
    }

    @Override
    public ResponseEntity<?> updateDetails(Details details) {
        try {
            String email = userService.getEmail();

            if (email.isBlank() || email.equals("anonymousUser")) {
                return ResponseEntity.badRequest().body("user not found");
            }
            ResponseEntity<?> resp = findByEmail(email);

            if (resp.getStatusCode().value() == 200) {
                Employee employee = (Employee) resp.getBody();

                assert employee != null;
                employee.setDetails(details);
                employeeRepository.save(employee);
                return ResponseEntity.ok().body(employee);
            }
            return resp;
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> savePassword(PasswordDTO passwordDTO) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(passwordDTO.getEmployeeId());
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            User user = employee.getUser();
            user.setPassword(passwordEncoder.encode(passwordDTO.getPassword()));
            userService.save(user);
            employeeRepository.save(employee);
            return ResponseEntity.ok("employee successfully set password");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("employee not found");
    }

    @Override
    public boolean check(UUID employeeId) {
        Optional<Employee> optEmployee = employeeRepository.findById(employeeId);
        return optEmployee.isPresent();
    }

    @Override
    public Employee findById(UUID employeeId) {
        Optional<Employee> byId = employeeRepository.findById(employeeId);
        return byId.orElse(null);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }


    @Override
    public List<GenderProjection> filterByProvince() {
        return employeeRepository.getGendersByProvince();
    }


    @Override
    public List<EmployeeByGenderDTO> getEmployeeByGender() {
        return employeeRepository.getEmployeeSummary();
    }

    @Override
    public ResponseEntity<?> getEmployeeInfo(UUID employeeId) {
        Optional<Employee> byId = employeeRepository.findById(employeeId);
        if (byId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("employee not found");
        }
        Employee employee = byId.get();

        List<Post> employeePosts = postService.getEmployeePosts(employeeId);

        String encodeToString = Base64.getEncoder().encodeToString(employee.getUser().getAttachmentContent().getContent());

        EmployeeInfoDTO employeeInfoDTO = EmployeeInfoDTO.builder()
                .fullName(employee.getUser().getFullName())
                .phone(employee.getUser().getPhone())
                .posts(employeePosts)
                .image(encodeToString)
                .build();
        return ResponseEntity.ok().body(employeeInfoDTO);
    }

    @Override
    public ResponseEntity<?> getTop(int quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body("quantity must be greater than 0");
        }
        List<TopRatingEmployeesProjection> topTen = employeeRepository.getEmployeesByRating(quantity);
        return ResponseEntity.ok().body(topTen);
    }

    @Override
    public Organization updateOrganization(OrganizationDTO organizationDTO, Employee employee) {

        Organization organization = employeeRepository.findOrganizationByEmployeeId(employee.getId());
        if (organization == null) {
            return null;
        }
        organization.setAllEmployeesAmount(organizationDTO.getAllEmployeesAmount());
        organization.setEmployeesOfAdministrationAmount(organizationDTO.getEmployeesOfAdministrationAmount());
        organization.setProfessorTeachersAmount(organizationDTO.getProfessorTeachersAmount());
        organization.setAllStudentsAmount(organizationDTO.getAllStudentsAmount());
        organizationService.save(organization);
        return organization;
    }

    @Override
    public ResponseEntity<?> isAvailableDetails() {
        String email = userService.getEmail();
        if (email.isBlank() || email.equals("anonymousUser")) {
            return ResponseEntity.badRequest().body("user not found");
        }
        boolean isAvailable = false;
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByUserEmailAndIsDeletedFalse(email);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            if (employee.getDetails() != null && employee.getDetails().getEntryDate() != null) {
                isAvailable = true;
            }
            return ResponseEntity.ok(isAvailable);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
    }

    @Override
    public ResponseEntity<?> uploadSalaryFile(MultipartFile file) {
        try {

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            String email = userService.getEmail();
            Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByUserEmailAndIsDeletedFalse(email);
            if (optionalEmployee.isEmpty()) {
                return ResponseEntity.badRequest().body("employee not found");
            }
            Employee employee = optionalEmployee.get();

            AttachmentContent attachmentContent = new AttachmentContent();
            attachmentContent.setContentType(file.getContentType());
            attachmentContent.setSize(file.getSize());
            attachmentContent.setFilename(file.getOriginalFilename());
            attachmentContent.setContent(file.getInputStream().readAllBytes());
            employee.setAttachmentContent(attachmentContent);
            employeeRepository.save(employee);
            return ResponseEntity.ok(attachmentContent);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Override
    public List<GenderProjection> getEmployeeByGenderRepublic() {
        return employeeRepository.getRepublicGenders();
    }
}