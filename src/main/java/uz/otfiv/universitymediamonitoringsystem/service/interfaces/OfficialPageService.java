package uz.otfiv.universitymediamonitoringsystem.service.interfaces;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.OfficialPageDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.OfficialPage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface OfficialPageService {

    OfficialPage save(OfficialPage officialPageDTO);

    Optional<OfficialPage> findById(UUID officialPageId);

    ResponseEntity<?> updateOfficialPage(OfficialPage officialPage, OfficialPageDTO officialPageDTO);

    ResponseEntity<?> delete(UUID officialPageId);

    boolean hasOfficialPages(String type);

    ResponseEntity<?> findByMessengerAndEmployeeId(OfficialPageDTO officialPageDTO);

    ResponseEntity<?> findAll();

    List<OfficialPage> findDeletedOfficialPagesByEmployeeId(UUID id);

    List<OfficialPage> findByEmployeeId(UUID employeeId);

    void absoluteDeleted(UUID id);

    ResponseEntity<?> recovered(UUID officialPageId);

    List<OfficialPage> findDeletedOfficialPagesByAdminId(UUID id);

    ResponseEntity<?> create(OfficialPageDTO officialPageDTO);
}
