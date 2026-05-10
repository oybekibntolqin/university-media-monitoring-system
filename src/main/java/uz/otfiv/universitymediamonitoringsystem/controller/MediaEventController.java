package uz.otfiv.universitymediamonitoringsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.otfiv.universitymediamonitoringsystem.dto.MediaEventDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.MediaEvent;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.MediaEventService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.ShowMediaService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media-event")
public class MediaEventController {

    private final MediaEventService mediaEventService;
    private final ShowMediaService showMediaService;

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        System.out.println("keldi");
        return ResponseEntity.ok(mediaEventService.findAll());
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<?> createMediaEvent(@RequestBody MediaEventDTO mediaEventDTO) {
        if (mediaEventDTO.getType().startsWith("/")) {
            mediaEventDTO.setType(mediaEventDTO.getType().substring(1));
        }
        return mediaEventService.saveByDTO(mediaEventDTO);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/get-media")
    public ResponseEntity<?> getMedia() {
        return ResponseEntity.ok(showMediaService.findAll());
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/recovered")
    public ResponseEntity<?> recovered(@RequestParam UUID mediaEventId) {
        return ResponseEntity.ok(mediaEventService.recovered(mediaEventId));
    }

    @DeleteMapping("/delete-event")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<?> deleteMediaEvent(@RequestParam UUID mediaEventId) {
        return mediaEventService.deleteMediaEvent(mediaEventId);
    }


    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<?> updateMediaEvent(@RequestBody MediaEvent mediaEvent) {
        return mediaEventService.update(mediaEvent);
    }

    @PutMapping("/editRating")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateMediaEvent(@RequestParam UUID mediaEventId, @RequestParam String rating) {
        return mediaEventService.changeRating();// todo don't finished
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @DeleteMapping("/absolute-delete")
    public ResponseEntity<?> absoluteDelete(@RequestParam UUID mediaEventId) {
        mediaEventService.absoluteDeleted(mediaEventId);
        return ResponseEntity.noContent().build();
    }
}
