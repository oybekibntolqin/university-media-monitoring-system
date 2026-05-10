package uz.otfiv.universitymediamonitoringsystem.projection;

public interface PostTypeCountProjection {
    String getPostType();

    Long getPostCount();

    Long getTotalPostCount();
}
