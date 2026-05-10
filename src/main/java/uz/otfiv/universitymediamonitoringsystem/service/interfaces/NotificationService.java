package uz.otfiv.universitymediamonitoringsystem.service.interfaces;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.NotificationDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Notification;

import java.util.Optional;
import java.util.UUID;

@Service
public interface NotificationService {

    ResponseEntity<?> createNotification(NotificationDTO notificationDTO);

    void save(Notification notification);

    ResponseEntity<?> getNotificationCount();

    ResponseEntity<?> getNotifications();

    ResponseEntity<?> deletedNotification(UUID notificationId);

    Optional<Notification> findById(UUID notificationId);

    ResponseEntity<?> getAll();

}
