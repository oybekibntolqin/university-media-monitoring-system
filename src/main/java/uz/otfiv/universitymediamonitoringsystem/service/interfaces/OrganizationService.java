package uz.otfiv.universitymediamonitoringsystem.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.OrganizationDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Organization;
import uz.otfiv.universitymediamonitoringsystem.projection.OrganizationAndEmployeeInfoProjection;
import uz.otfiv.universitymediamonitoringsystem.projection.OrganizationsPostsProjection;

import java.util.List;
import java.util.UUID;

@Service
public interface OrganizationService {

    void save(Organization ministry);

    List<OrganizationAndEmployeeInfoProjection> getOrganizationTheEmployees();

    Organization findById(UUID organizationId);

    List<Organization> findAll();

    ResponseEntity<?> fillOrganization(OrganizationDTO organizationDTO);

    ResponseEntity<?> getUniversitiesByProvince();

    ResponseEntity<List<OrganizationsPostsProjection>> getTopUniversitiesByProvince(String province);

    ResponseEntity<?> getOrganizationPosts(UUID organizationId);

    ResponseEntity<?> getRatingAllUniversities();
}
