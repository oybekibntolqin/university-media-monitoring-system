package uz.otfiv.universitymediamonitoringsystem.projection;

import java.util.UUID;

public interface TopLastMonthPostsProjection {
    UUID getId();

    String getName();

    Integer getTotalPostCount();
}
