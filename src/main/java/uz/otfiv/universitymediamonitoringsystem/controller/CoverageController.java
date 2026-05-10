package uz.otfiv.universitymediamonitoringsystem.controller;

import uz.otfiv.universitymediamonitoringsystem.dto.CoverageDTO;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.CoverageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coverage")
@RequiredArgsConstructor
public class CoverageController {
    private final CoverageService coverageService;

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return coverageService.getAll();
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/recovered")
    public ResponseEntity<?> getRecovered(@RequestParam UUID coverageId) {
        return coverageService.recovered(coverageId);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @PostMapping(value = "/add")
    public ResponseEntity<?> add(@Valid @RequestBody CoverageDTO coverageDTO) {
        return coverageService.create(coverageDTO);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam UUID id) {
        return coverageService.delete(id);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @DeleteMapping("/absolute-delete")
    public ResponseEntity<?> absoluteDelete(@RequestParam UUID coverageId) {
        coverageService.absoluteDeleted(coverageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
