package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.otfiv.universitymediamonitoringsystem.entity.MediaEvent;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.MediaEventType;
import uz.otfiv.universitymediamonitoringsystem.projection.GetAllMediaEventsProjection;
import uz.otfiv.universitymediamonitoringsystem.projection.GetMediaEventCountProjection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaEventRepository extends JpaRepository<MediaEvent, UUID> {

    @Query(value = """
            select * from media_event m inner join public.media_event_stuff mv
                on mv.media_event_id = m.id inner join media_event_messengers ms on m.id = ms.media_event_id
                     inner join media_event_newspapers mn on m.id = mn.media_event_id
                     inner join "public".media_event_radio_channels merc on m.id = merc.media_event_id
                     where m.is_deleted = true and m.employee_id = ?1 and m.media_event_type = ?2
            """, nativeQuery = true)
    List<MediaEvent> findAllByEmployeeId(UUID employeeId, MediaEventType mediaEventType);

    Optional<MediaEvent> findByIdAndIsDeletedFalse(UUID mediaEventId);

    List<MediaEvent> findMediaEventsByEmployeeIdAndIsDeletedTrue(UUID employeeId);

    List<GetAllMediaEventsProjection> findMediaEventsByEmployeeIdAndIsDeletedFalse(UUID employeeId);

    Integer countAllByIsDeletedFalse();

    List<GetAllMediaEventsProjection> findMediaEventsByAdminIdAndIsDeletedFalse(UUID id);

    List<MediaEvent> findMediaEventsByAdminIdAndIsDeletedTrue(UUID adminId);

    List<MediaEvent> findAllByIsDeletedFalse();

    @Query(value = """
            SELECT m.event_name AS eventName,
                   m.media_event_type AS mediaEventType,
                   m.date_of_event AS dateOfEvent,
                   JSON_AGG(m.stuff) AS stuff,
                   JSON_AGG(JSON_BUILD_OBJECT('name', mtv.tv_name, 'link', mtv.link)) AS tv_channels
            FROM media_event m
            LEFT JOIN media_event_tv_channels mtv ON mtv.media_event_id = m.id
            WHERE m.employee_id = ?1
            GROUP BY m.id;
            """, nativeQuery = true)
    List<GetAllMediaEventsProjection> getAllByEmployeeId(UUID employeeId);


    @Query(value = """
                           SELECT
                me.media_event_type AS mediaEventType,
               COUNT(me) AS mediaEventCount,
              SUM(CASE WHEN me.media_event_type = 'PRESS_CONFERENCE' THEN 1 ELSE 0 END) AS pressConferenceCount,
             SUM(CASE WHEN me.media_event_type = 'BRIEFING' THEN 1 ELSE 0 END) AS briefingCount,
            SUM(CASE WHEN me.media_event_type = 'PRESS_TOUR' THEN 1 ELSE 0 END) AS pressTourCount
                FROM media_event me
                WHERE me.is_deleted = false AND me.employee_id IS NOT NULL
               GROUP BY me.media_event_type 
            """, nativeQuery = true)
    List<GetMediaEventCountProjection> getMediaEventCount();
}