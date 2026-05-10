package uz.otfiv.universitymediamonitoringsystem.service.interfaces;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.MediaEventDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.MediaEvent;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.MediaEventType;
import uz.otfiv.universitymediamonitoringsystem.projection.GetAllMediaEventsProjection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface MediaEventService {
    MediaEvent save(MediaEvent mediaEvent);

    ResponseEntity<?> saveByDTO(MediaEventDTO mediaEventDTO);

    ResponseEntity<?> update(MediaEvent mediaEvent);

    ResponseEntity<?> findAll(MediaEventType type);

    ResponseEntity<?> findAll();

    ResponseEntity<?> deleteMediaEvent(UUID mediaEventId);

    ResponseEntity<?> changeRating();

    List<MediaEvent> findDeletedEvents(UUID id);

    List<GetAllMediaEventsProjection> findByEmployeeId(UUID id);

    Integer getCount();

    Optional<MediaEvent> findById(UUID postId);

    void absoluteDeleted(UUID id);

    ResponseEntity<?> recovered(UUID mediaEventId);

    List<MediaEvent> findDeletedEventsAdmin(UUID id);

    List<GetAllMediaEventsProjection> findAdminMediaEvents(UUID adminId);

    List<MediaEvent> findAllMediaEvents();

    ResponseEntity<?> getMediaEventCount();
}
