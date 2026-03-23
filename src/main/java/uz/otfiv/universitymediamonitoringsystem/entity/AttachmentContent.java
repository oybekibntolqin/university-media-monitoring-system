package uz.otfiv.universitymediamonitoringsystem.entity;

import jakarta.persistence.Entity;
import lombok.*;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class AttachmentContent extends AbsEntity {
    private String filename;
    private String contentType;
    private Long size;

    private byte[] content;
}
