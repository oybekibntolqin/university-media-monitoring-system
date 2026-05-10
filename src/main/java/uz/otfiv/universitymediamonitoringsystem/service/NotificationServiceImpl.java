package uz.otfiv.universitymediamonitoringsystem.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.NotificationDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.*;
import uz.otfiv.universitymediamonitoringsystem.repo.NotificationRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.*;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final PostService postService;
    private final MessageService messageService;
    private final AdminService adminService;
    private final EmployeeService employeeService;
    private final MediaEventService mediaEventService;
    private final OfficialPageService officialPageService;
    private final MaterialService materialService;
    private final BroadcastAndVoiceChatService broadcastAndVoiceChatService;
    private final CoverageService coverageService;
    private final MediaProjectService mediaProjectService;
    private final UserService userService;
    private final ForeignMaterialService foreignMaterialService;

    @Override
    public ResponseEntity<?> createNotification(NotificationDTO notificationDTO) {

        String postType = notificationDTO.getPostType();

        String email = userService.getEmail();

        Optional<Admin> optionalAdmin = adminService.findByEmail(email);
        Message message = new Message();

        Notification notification = new Notification();

        if (optionalAdmin.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        switch (postType) {
            case "POSTS" -> {
                Optional<Post> optionalPost = postService.findById(notificationDTO.getPostId());

                if (optionalPost.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }

                Post post = optionalPost.get();
                message.setContent("POST");
                notification.setEmployee(post.getEmployee());

                postService.absoluteDeleted(post.getId());
            }
            case "MEDIA_EVENT" -> {
                Optional<MediaEvent> optionMediaEvent = mediaEventService.findById(notificationDTO.getPostId());

                if (optionMediaEvent.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }

                MediaEvent mediaEvent = optionMediaEvent.get();
                message.setContent("MEDIA_EVENT");
                notification.setEmployee(mediaEvent.getEmployee());

                mediaEventService.absoluteDeleted(mediaEvent.getId());
            }
            case "OFFICIAL_PAGE" -> {
                Optional<OfficialPage> optionalOfficialPage = officialPageService.findById(notificationDTO.getPostId());

                if (optionalOfficialPage.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }

                OfficialPage officialPage = optionalOfficialPage.get();
                message.setContent("OFFICIAL_PAGE");
                notification.setEmployee(officialPage.getEmployee());

                officialPageService.absoluteDeleted(officialPage.getId());
            }
            case "MATERIALS" -> {

                Optional<Material> optionalMaterial = materialService.findById(notificationDTO.getPostId());
                if (optionalMaterial.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }

                Material material = optionalMaterial.get();
                message.setContent("MATERIAL");
                notification.setEmployee(material.getEmployee());

                materialService.absoluteDeleted(material.getId());
            }
            case "ONLINE_BROADCAST" -> {
                Optional<OnlineBroadcastAndVoiceChatPost> optionBroadCast = broadcastAndVoiceChatService.findByChatId(notificationDTO.getPostId());
                if (optionBroadCast.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }

                OnlineBroadcastAndVoiceChatPost onlineBroadcastAndVoiceChatPost = optionBroadCast.get();
                message.setContent("ONLINE_BROADCAST");
                notification.setEmployee(onlineBroadcastAndVoiceChatPost.getEmployee());

                broadcastAndVoiceChatService.absoluteDeleted(onlineBroadcastAndVoiceChatPost.getId());
            }
            case "COVERAGES" -> {

                Optional<Coverage> optionalCoverage = coverageService.findById(notificationDTO.getPostId());
                if (optionalCoverage.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }

                Coverage coverage = optionalCoverage.get();
                message.setContent("COVERAGE");
                notification.setEmployee(coverage.getEmployee());

                coverageService.absoluteDeleted(coverage.getId());
            }
            case "MEDIA_PROJECTS" -> {
                Optional<MediaProject> optionalMediaProject = mediaProjectService.findById(notificationDTO.getPostId());
                if (optionalMediaProject.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }
                MediaProject mediaProject = optionalMediaProject.get();
                message.setContent("MEDIA_PROJECT");
                notification.setEmployee(mediaProject.getEmployee());

                mediaProjectService.absoluteDeleted(mediaProject.getId());
            }
            case "FOREIGN_MATERIAL" -> {
                Optional<ForeignMaterials> optionalForeignMaterials = foreignMaterialService.findById(notificationDTO.getPostId());
                if (optionalForeignMaterials.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }
                ForeignMaterials foreignMaterials = optionalForeignMaterials.get();
                message.setContent("FOREIGN_MATERIAL");
                notification.setEmployee(foreignMaterials.getEmployee());
                foreignMaterialService.absoluteDeleted(foreignMaterials.getId());
            }
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }
        message.setReason(notificationDTO.getReasons());
        messageService.save(message);
        notification.setMessage(message);
        notification.setAdmin(optionalAdmin.get());
        save(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @Override
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public ResponseEntity<?> getNotificationCount() {
        String email = userService.getEmail();
        ResponseEntity<?> resp = employeeService.findByEmail(email);
        if (resp.getStatusCode().value() != 200) {
            return ResponseEntity.badRequest().build();
        }
        Employee employee = (Employee) resp.getBody();

        assert employee != null;
        return ResponseEntity.ok(notificationRepository.getNotificationCountByEmployeeId(employee.getId()));
    }

    @Override
    public ResponseEntity<?> getNotifications() {

        String email = userService.getEmail();
        ResponseEntity<?> resp = employeeService.findByEmail(email);
        if (resp.getStatusCode().value() != 200) {
            return ResponseEntity.badRequest().build();
        }
        Employee employee = (Employee) resp.getBody();

        assert employee != null;

        return ResponseEntity.ok(notificationRepository.findNotificationsByEmployeeIdAndIsReadFalse(employee.getId()));
    }

    @Override
    public ResponseEntity<?> deletedNotification(UUID notificationId) {
        Optional<Notification> optionalNotification = findById(notificationId);
        if (optionalNotification.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        optionalNotification.get().setIsDeleted(true);
        save(optionalNotification.get());

        return ResponseEntity.noContent().build();
    }

    @Override
    public Optional<Notification> findById(UUID notificationId) {
        return notificationRepository.findById(notificationId);
    }

    @Override
    public ResponseEntity<?> getAll() {
        String email = userService.getEmail();
        ResponseEntity<?> resp = employeeService.findByEmail(email);
        if (resp.getStatusCode().value() != 200) {
            return ResponseEntity.badRequest().build();
        }
        Employee employee = (Employee) resp.getBody();

        assert employee != null;

        notificationRepository.updateNotificationsIsReadTrue(employee.getId());

        return ResponseEntity.ok(notificationRepository.findNotificationsByEmployeeIdAndIsReadTrueAndIsDeletedFalse(employee.getId()));
    }
}
