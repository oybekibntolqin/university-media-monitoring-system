package uz.otfiv.universitymediamonitoringsystem.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.otfiv.universitymediamonitoringsystem.dto.OfficialPageDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.OfficialPage;
import uz.otfiv.universitymediamonitoringsystem.exception.GlobalExceptionHandler;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.OfficialPageService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/official-page")
@RequiredArgsConstructor
public class OfficialPageController {
    private final OfficialPageService officialPageService;
    private final GlobalExceptionHandler globalExceptionHandler;

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return officialPageService.findAll();
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/recovered")
    public ResponseEntity<?> getRecovered(@RequestParam UUID officialPageId) {
        return ResponseEntity.ok(officialPageService.recovered(officialPageId));
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody OfficialPageDTO officialPageDTO) {
        return officialPageService.create(officialPageDTO);
    }


    @PutMapping("/check")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public boolean checkOfficialPage(@RequestParam String type) {
        return officialPageService.hasOfficialPages(type);
    }


    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<?> update(@RequestParam UUID officialPageId, @RequestBody OfficialPageDTO officialPageDTO) {
        try {
            Optional<OfficialPage> optionalOfficialPage = officialPageService.findById(officialPageId);
            if (optionalOfficialPage.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("officialPage topilmadi");
            }
            OfficialPage officialPage = optionalOfficialPage.get();

            ResponseEntity<?> updatedOfficialPage = officialPageService.updateOfficialPage(officialPage, officialPageDTO);
            return ResponseEntity.ok().body(updatedOfficialPage);
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<?> delete(@RequestParam UUID officialPageId) {
        try {
            officialPageService.delete(officialPageId);
            return ResponseEntity.ok("official page deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @DeleteMapping("/absolute-delete")
    public ResponseEntity<?> absoluteDelete(@RequestParam UUID officialPageId) {
        officialPageService.absoluteDeleted(officialPageId);
        return ResponseEntity.noContent().build();
    }

}
