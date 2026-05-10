package uz.otfiv.universitymediamonitoringsystem.projection;

import java.time.LocalDate;
import java.util.UUID;

public interface GetAllMaterialsProjection {
    UUID getId();

    String getTopic();

    LocalDate getPublishDate();

    String getMaterialType();

    String getMassMedia();

    String getSocialMediaName();

    String getLink();

    String getGrade();
}
