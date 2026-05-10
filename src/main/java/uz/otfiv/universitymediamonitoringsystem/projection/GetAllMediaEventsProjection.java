package uz.otfiv.universitymediamonitoringsystem.projection;


import uz.otfiv.universitymediamonitoringsystem.entity.enums.MediaEventType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GetAllMediaEventsProjection {
    UUID getId();

    String getEventName();

    MediaEventType getMediaEventType();

    Map<String, String> getTv_channels();//<TV_name, link>

    Map<String, String> getRadio_channels();

    Map<String, String> getNewspapers();

    Map<String, String> getMessengers();

    Map<String, String> getWeb_sites(); //telegram-blogs,youtube channel-name,facebook

    LocalDateTime getDateOfEvent();

    List<String> getStuff();

    Integer getGrade();
}
