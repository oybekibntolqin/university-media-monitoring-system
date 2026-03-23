package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.otfiv.universitymediamonitoringsystem.converter.GradeConverter;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Messenger;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder

@Table(name = "online_broadcast_and_voice_chat_post", indexes = {
        @Index(name = "idx_broadcast_is_deleted", columnList = "is_deleted")
})
public class OnlineBroadcastAndVoiceChatPost extends AbsEntity {
    private String title;
    private LocalDate eventDate;
    @ElementCollection
    private List<String> stuff;
    @Enumerated(value = EnumType.STRING)
    private Messenger messenger;
    private Integer numberOfPeople; // chatda yoki efirda qatnashgan insonlar soni
    private String link;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Admin admin;
    @Convert(converter = GradeConverter.class)
    private Grade grade;
}