package uz.otfiv.universitymediamonitoringsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.NotificationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/get-count")
    public ResponseEntity<?> getNotificationCount() {
        return notificationService.getNotificationCount();
    }

    @GetMapping("/get-notifications")
    public ResponseEntity<?> getNotifications() {
        return notificationService.getNotifications();
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return notificationService.getAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @DeleteMapping("/delete-notification")
    public ResponseEntity<?> deletedNotification(@RequestParam UUID notificationId) {
        return notificationService.deletedNotification(notificationId);
    }

}
