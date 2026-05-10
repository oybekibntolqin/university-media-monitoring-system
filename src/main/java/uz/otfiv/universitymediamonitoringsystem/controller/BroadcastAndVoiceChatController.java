package uz.otfiv.universitymediamonitoringsystem.controller;

;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.otfiv.universitymediamonitoringsystem.dto.BroadcastDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.OnlineBroadcastAndVoiceChatPost;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.BroadcastAndVoiceChatService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/broadcast")
@RequiredArgsConstructor
public class BroadcastAndVoiceChatController {
    private final BroadcastAndVoiceChatService broadcastAndVoiceChatService;

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return broadcastAndVoiceChatService.findAll();
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @PostMapping("/create-broadcast")
    public ResponseEntity<?> createBroadcast(@Valid @RequestBody BroadcastDTO broadcastDTO) {
        return broadcastAndVoiceChatService.createBroadcast(broadcastDTO);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/recovered")
    public ResponseEntity<?> recovered(@RequestParam UUID broadcastId) {
        return broadcastAndVoiceChatService.recovered(broadcastId);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @DeleteMapping("/delete-broadcast")
    public ResponseEntity<?> deleteBroadcast(@RequestParam UUID broadcastId) {
        return broadcastAndVoiceChatService.delete(broadcastId);
    }

    @PutMapping("/edit-broadcast")
    public ResponseEntity<?> editBroadcast(@RequestBody BroadcastDTO broadcastDTO, @RequestParam UUID broadcastId) {
        Optional<OnlineBroadcastAndVoiceChatPost> chatOptional = broadcastAndVoiceChatService.findByChatId(broadcastId);

        if (chatOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return broadcastAndVoiceChatService.editBroadcast(chatOptional.get(), broadcastDTO);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @DeleteMapping("/absolute-delete")
    public ResponseEntity<?> absoluteDelete(@RequestParam UUID broadcastId) {
        broadcastAndVoiceChatService.absoluteDeleted(broadcastId);
        return ResponseEntity.noContent().build();
    }
}