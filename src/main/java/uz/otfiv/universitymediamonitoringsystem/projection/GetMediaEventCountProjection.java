package uz.otfiv.universitymediamonitoringsystem.projection;

public interface GetMediaEventCountProjection {
    String getMediaEventType();

    Integer getMediaEventCount();

    Integer getPressConferenceCount();

    Integer getBriefingCount();

    Integer getPressTourCount();
}
