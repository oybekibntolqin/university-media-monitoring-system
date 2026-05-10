package uz.otfiv.universitymediamonitoringsystem.projection;

public interface        OrganizationAllPostsCountProjection {
    String getName();
    Integer getPostsCount();
    Integer getMediaEventsCount();
    Integer getCoveragesCount();
    Integer getMediaProjectsCount();
    Integer getMaterialsCount();
    Integer getBroadcastsCount();
    Integer getForeignMaterialsCount();
}
