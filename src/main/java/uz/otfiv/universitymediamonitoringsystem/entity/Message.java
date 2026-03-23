package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.Entity;
import lombok.*;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Message extends AbsEntity {
    private String content;
    private List<String> reason;


}
