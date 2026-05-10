package uz.otfiv.universitymediamonitoringsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class NotificationDTO {
    private String postType;
    private UUID postId;
    private List<String> reasons;
}
