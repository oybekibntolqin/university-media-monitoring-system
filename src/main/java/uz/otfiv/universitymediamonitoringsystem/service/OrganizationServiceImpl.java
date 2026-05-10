package uz.otfiv.universitymediamonitoringsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.OrganizationDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.Organization;
import uz.otfiv.universitymediamonitoringsystem.projection.OrganizationAllPostsCountProjection;
import uz.otfiv.universitymediamonitoringsystem.projection.OrganizationAndEmployeeInfoProjection;
import uz.otfiv.universitymediamonitoringsystem.projection.OrganizationsPostsProjection;
import uz.otfiv.universitymediamonitoringsystem.repo.OrganizationRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.EmployeeService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.OrganizationService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.UserService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final EmployeeService employeeService;
    private final UserService userService;

    @Override
    public void save(Organization ministry) {
        organizationRepository.save(ministry);
    }

    @Override
    public List<OrganizationAndEmployeeInfoProjection> getOrganizationTheEmployees() {
        return List.of();
    }

    @Override
    public Organization findById(UUID organizationId) {
        return organizationRepository.findById(organizationId).orElse(null);
    }

    @Override
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    @Override
    public ResponseEntity<?> fillOrganization(OrganizationDTO organizationDTO) {
        String email = userService.getEmail();
        ResponseEntity<?> byEmail = employeeService.findByEmail(email);
        if (byEmail.getStatusCode().value() != 200) {
            return ResponseEntity.badRequest().body(byEmail.getBody());
        }
        Employee employee = (Employee) byEmail.getBody();

        Organization organization = employeeService.updateOrganization(organizationDTO, employee);
        if (organization == null) {
            return ResponseEntity.badRequest().body("Organization not found");
        }

        return ResponseEntity.ok(organization);
    }

    @Override
    public ResponseEntity<?> getUniversitiesByProvince() {
        return ResponseEntity.ok(organizationRepository.findInstituteCountByProvince());
    }

    @Override
    public ResponseEntity<List<OrganizationsPostsProjection>> getTopUniversitiesByProvince(String province) {
        List<OrganizationsPostsProjection> topOrganizationsByProvince = organizationRepository.getTopOrganizationsByProvince(province);
        return ResponseEntity.ok(topOrganizationsByProvince);
    }

    @Override
    public ResponseEntity<?> getOrganizationPosts(UUID organizationId) {
        List<OrganizationAllPostsCountProjection> organizationAllPostsCount = organizationRepository.getOrganizationAllPostsCount(organizationId);
        return ResponseEntity.ok(organizationAllPostsCount);
    }

    @Override
    public ResponseEntity<?> getRatingAllUniversities() {
        return ResponseEntity.ok(organizationRepository.getRatingUniversities());
    }
}
