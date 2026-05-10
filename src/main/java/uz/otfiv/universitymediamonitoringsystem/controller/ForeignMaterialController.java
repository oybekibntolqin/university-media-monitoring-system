package uz.otfiv.universitymediamonitoringsystem.controller;

import uz.otfiv.universitymediamonitoringsystem.dto.ForeignMaterialDTO;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.ForeignMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/foreign-oav")
@RequiredArgsConstructor
public class ForeignMaterialController {
    private final ForeignMaterialService foreignMaterialService;

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return foreignMaterialService.getAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/recovered")
    public ResponseEntity<?> getRecovered(@RequestParam UUID foreignId) {
        return foreignMaterialService.recovered(foreignId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody ForeignMaterialDTO foreignMaterialDTO) {
        return foreignMaterialService.create(foreignMaterialDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @DeleteMapping("/delete-foreign-oav")
    public ResponseEntity<?> delete(@RequestParam UUID foreignId) {
        return foreignMaterialService.deleted(foreignId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @DeleteMapping("/absolute-delete")
    public ResponseEntity<?> absoluteDelete(@RequestParam UUID foreignId) {
        return foreignMaterialService.absoluteDeleted(foreignId);
    }
}
