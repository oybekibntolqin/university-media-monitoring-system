package uz.otfiv.universitymediamonitoringsystem.controller;

import uz.otfiv.universitymediamonitoringsystem.dto.MediaProjectDTO;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.MediaProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/media-project")
@RequiredArgsConstructor
public class MediaProjectController {
    private final MediaProjectService mediaProjectService;

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<?> getMediaProject() {
        return mediaProjectService.getAll();
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/recovered")
    public ResponseEntity<?> getMediaProjectRecovered(@RequestParam UUID mediaProjectId) {
        return ResponseEntity.ok(mediaProjectService.recovered(mediaProjectId));
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addMediaEvent(@Valid @RequestBody MediaProjectDTO mediaProjectDTO) {
        return mediaProjectService.create(mediaProjectDTO);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @DeleteMapping("delete")
    public ResponseEntity<?> deleteMediaEvent(@RequestParam UUID id) {
        return mediaProjectService.delete(id);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @DeleteMapping("/absolute-delete")
    public ResponseEntity<?> absoluteMediaEvent(@RequestParam UUID mediaProjectId) {
        mediaProjectService.absoluteDeleted(mediaProjectId);
        return ResponseEntity.noContent().build();
    }
}
