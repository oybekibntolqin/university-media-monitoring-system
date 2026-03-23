package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.otfiv.universitymediamonitoringsystem.converter.GradeConverter;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.MediaEventType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "media_event", indexes = {
        @Index(name = "idx_media_event_is_deleted", columnList = "is_deleted")
})
public class MediaEvent extends AbsEntity {//media tadbirlar
    private String eventName;
    @Enumerated(EnumType.STRING)
    private MediaEventType mediaEventType;

    @ElementCollection
    @MapKeyColumn
    private Map<String, String> tv_channels = new HashMap<>();//<TV_name, link>
    @ElementCollection
    @MapKeyColumn
    private Map<String, String> radio_channels = new HashMap<>();
    @ElementCollection
    @MapKeyColumn
    private Map<String, String> newspapers = new HashMap<>();
    @ElementCollection
    @MapKeyColumn
    private Map<String, String> messengers = new HashMap<>();
    @ElementCollection
    @MapKeyColumn
    private Map<String, String> web_sites = new HashMap<>(); //telegram-blogs,youtube channel-name,facebook
    private LocalDateTime dateOfEvent;
    @ElementCollection
    @CollectionTable(name = "media_event_stuff", joinColumns = @JoinColumn(name = "media_event_id"))
    private List<String> stuff = new ArrayList<>();
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Admin admin;
    @Convert(converter = GradeConverter.class)
    private Grade grade;

}
