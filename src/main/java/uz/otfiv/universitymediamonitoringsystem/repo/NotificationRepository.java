package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uz.otfiv.universitymediamonitoringsystem.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    @Query(value = "SELECT COUNT(n) FROM Notification n WHERE n.is_read = false and n.employee_id = ?1", nativeQuery = true)
    Integer getNotificationCountByEmployeeId(UUID employeeId);

    List<Notification> findNotificationsByEmployeeIdAndIsReadFalse(UUID employeeId);

    @Modifying
    @Transactional
    @Query(value = "update notification set is_read=true where employee_id = ?1", nativeQuery = true)
    void updateNotificationsIsReadTrue(UUID id);


    List<Notification> findNotificationsByEmployeeIdAndIsReadTrueAndIsDeletedFalse(UUID id);
}