package uz.otfiv.universitymediamonitoringsystem.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.otfiv.universitymediamonitoringsystem.dto.MaterialDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Material;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.MaterialService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/material")
public class MaterialController {
    private final MaterialService materialService;

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return materialService.findAll();
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/read")
    public ResponseEntity<?> get() {
        return materialService.getMaterials();
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/recovered")
    public ResponseEntity<?> getRecovered(@RequestParam UUID materialId) {
        return ResponseEntity.ok(materialService.recovered(materialId));
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createMaterial(@Valid @RequestBody MaterialDTO materialDTO) {
        return materialService.create(materialDTO);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @PatchMapping("/update")
    public ResponseEntity<?> updateMaterial(@RequestBody Material material) {
        return materialService.update(material);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMaterial(@RequestParam(name = "materialId") UUID materialId) {
        return materialService.delete(materialId);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @DeleteMapping("/absolute-delete")
    public ResponseEntity<?> absoluteDelete(@RequestParam(name = "materialId") UUID materialId) {
        return materialService.absoluteDeleted(materialId);
    }
}
