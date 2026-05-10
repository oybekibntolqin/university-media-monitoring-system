package uz.otfiv.universitymediamonitoringsystem.projection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface GetAllPostsProjection {
    UUID employeeId();

    UUID getId();

    String getLink();

    LocalDateTime getDateTime();

    String getShowedUser();

    String getMedia();

    String getShow();

    String getStuff();

    String getScale();

    String getPostType();

    String getGrade();
}
