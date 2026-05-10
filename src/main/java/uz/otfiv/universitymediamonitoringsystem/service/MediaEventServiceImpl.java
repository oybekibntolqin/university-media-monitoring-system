package uz.otfiv.universitymediamonitoringsystem.service;

import uz.otfiv.universitymediamonitoringsystem.dto.MediaEventDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Admin;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.MediaEvent;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.MediaEventType;
import uz.otfiv.universitymediamonitoringsystem.projection.GetAllMediaEventsProjection;
import uz.otfiv.universitymediamonitoringsystem.repo.MediaEventRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaEventServiceImpl implements MediaEventService {
    private final MediaEventRepository mediaEventRepository;
    private final EmployeeService employeeService;
    private final AdminService adminService;
    private final UserService userService;

    @Override
    public MediaEvent save(MediaEvent mediaEvent) {
        return mediaEventRepository.save(mediaEvent);
    }

    @Override
    public ResponseEntity<?> saveByDTO(MediaEventDTO mediaEventDTO) {
        String email = userService.getEmail();

        ResponseEntity<?> resp = employeeService.findByEmail(email);

        MediaEvent mediaEvent = new MediaEvent();
        mediaEvent.setGrade(Grade.GRADE_FIVE);

        if (resp.getStatusCode().value() == 200) {
            Employee employee = (Employee) resp.getBody();
            mediaEvent.setEmployee(employee);
            assert employee != null;
            if (employee.getRating() != null) {
                employee.setRating(employee.getRating() + mediaEvent.getGrade().getValue());
            } else {
                employee.setRating(mediaEvent.getGrade().getValue());
            }
            employeeService.save(employee);
        } else {
            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();
                mediaEvent.setAdmin(admin);
            } else {
                return ResponseEntity.badRequest().body("User not found");
            }
        }

        MediaEventType mediaEventType = MediaEventType.fromValue(mediaEventDTO.getType());
        mediaEvent.setMediaEventType(mediaEventType);
        mediaEvent.setDateOfEvent(mediaEventDTO.getDateOfEvent());
        mediaEvent.setEventName(mediaEventDTO.getEventName());
        mediaEvent.setStuff(mediaEventDTO.getStuffs());
        mediaEvent.setTv_channels(mediaEventDTO.getTv_channels());
        mediaEvent.setMessengers(mediaEventDTO.getMessengers());
        mediaEvent.setNewspapers(mediaEventDTO.getNewspapers());
        mediaEvent.setWeb_sites(mediaEventDTO.getWeb_sites());
        mediaEvent.setRadio_channels(mediaEventDTO.getRadio_channels());
        MediaEvent save = mediaEventRepository.save(mediaEvent);
        return ResponseEntity.ok(save);
    }


    @Override
    public ResponseEntity<?> update(MediaEvent mediaEvent) {
        return ResponseEntity.ok(mediaEventRepository.save(mediaEvent));
    }

    @Override
    public ResponseEntity<?> findAll(MediaEventType type) {
        String email = userService.getEmail();
        ResponseEntity<?> resp = employeeService.findByEmail(email);

        if (resp.getStatusCode().value() != 200) {
            return ResponseEntity.badRequest().body(resp);
        }

        Employee employee = (Employee) resp.getBody();

        assert employee != null;
        List<MediaEvent> mediaEvents = mediaEventRepository.findAllByEmployeeId(employee.getId(), type);

        return ResponseEntity.ok(mediaEvents);
    }

    @Override
    public ResponseEntity<?> findAll() {
        String email = userService.getEmail();
        ResponseEntity<?> resp = employeeService.findByEmail(email);

        if (resp.getStatusCode().value() == 200) {
            Employee employee = (Employee) resp.getBody();

            assert employee != null;

            return ResponseEntity.status(HttpStatus.CREATED).body(mediaEventRepository.findMediaEventsByEmployeeIdAndIsDeletedFalse(employee.getId()));
        } else {

            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                return ResponseEntity.ok(mediaEventRepository.findMediaEventsByAdminIdAndIsDeletedFalse(optionalAdmin.get().getId()));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> deleteMediaEvent(UUID mediaEventId) {
        Optional<MediaEvent> mediaEventOptional = mediaEventRepository.findByIdAndIsDeletedFalse(mediaEventId);
        if (mediaEventOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        MediaEvent mediaEvent = mediaEventOptional.get();
        mediaEvent.setIsDeleted(true);
        mediaEventRepository.save(mediaEvent);
        return ResponseEntity.ok("Media event deleted successfully");
    }

    @Override
    public ResponseEntity<?> changeRating() {
        return null;
    }

    @Override
    public List<MediaEvent> findDeletedEvents(UUID employeeId) {
        return mediaEventRepository.findMediaEventsByEmployeeIdAndIsDeletedTrue(employeeId);
    }

    @Override
    public List<GetAllMediaEventsProjection> findByEmployeeId(UUID id) {
        return mediaEventRepository.findMediaEventsByEmployeeIdAndIsDeletedFalse(id);
    }

    @Override
    public Integer getCount() {
        return mediaEventRepository.countAllByIsDeletedFalse();
    }

    @Override
    public Optional<MediaEvent> findById(UUID postId) {
        return mediaEventRepository.findById(postId);
    }

    @Override
    public void absoluteDeleted(UUID id) {
        mediaEventRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<?> recovered(UUID mediaEventId) {
        Optional<MediaEvent> optionalMediaEvent = findById(mediaEventId);
        if (optionalMediaEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        optionalMediaEvent.get().setIsDeleted(false);
        mediaEventRepository.save(optionalMediaEvent.get());
        return ResponseEntity.ok("Media event recovered successfully");
    }

    @Override
    public List<MediaEvent> findDeletedEventsAdmin(UUID adminId) {
        return mediaEventRepository.findMediaEventsByAdminIdAndIsDeletedTrue(adminId);
    }

    @Override
    public List<GetAllMediaEventsProjection> findAdminMediaEvents(UUID adminId) {
        return mediaEventRepository.findMediaEventsByAdminIdAndIsDeletedFalse(adminId);
    }

    @Override
    public List<MediaEvent> findAllMediaEvents() {
        return mediaEventRepository.findAllByIsDeletedFalse();
    }

    @Override
    public ResponseEntity<?> getMediaEventCount() {
        return ResponseEntity.ok(mediaEventRepository.getMediaEventCount());
    }

}