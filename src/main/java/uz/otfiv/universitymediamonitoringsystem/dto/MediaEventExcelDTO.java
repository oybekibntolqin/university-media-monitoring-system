package uz.otfiv.universitymediamonitoringsystem.dto;

import lombok.*;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.MediaEventType;
import uz.otfiv.universitymediamonitoringsystem.projection.GetAllMediaEventsProjection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Builder
public class MediaEventExcelDTO implements GetAllMediaEventsProjection {

    private UUID getId;
    private String getEventName;

    private MediaEventType getMediaEventType;

    private Map<String, String> getTv_channels;//<TV_name, link>

    private Map<String, String> getRadio_channels;

    private Map<String, String> getNewspapers;

    private Map<String, String> getMessengers;

    private Map<String, String> getWeb_sites; //telegram-blogs,youtube channel-name,facebook

    private LocalDateTime getDateOfEvent;

    private List<String> getStuff;

    private Integer getGrade;

    @Override
    public UUID getId() {
        return this.getId;
    }

    @Override
    public String getEventName() {
        return this.getEventName;
    }

    @Override
    public MediaEventType getMediaEventType() {
        return this.getMediaEventType;
    }

    @Override
    public Map<String, String> getTv_channels() {
        return this.getTv_channels;
    }

    @Override
    public Map<String, String> getRadio_channels() {
        return this.getRadio_channels;
    }

    @Override
    public Map<String, String> getNewspapers() {
        return this.getNewspapers;
    }

    @Override
    public Map<String, String> getMessengers() {
        return this.getMessengers;
    }

    @Override
    public Map<String, String> getWeb_sites() {
        return this.getWeb_sites;
    }

    @Override
    public LocalDateTime getDateOfEvent() {
        return this.getDateOfEvent;
    }

    @Override
    public List<String> getStuff() {
        return this.getStuff;
    }

    @Override
    public Integer getGrade() {
        return this.getGrade;
    }
}
