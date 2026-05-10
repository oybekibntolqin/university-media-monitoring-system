package uz.otfiv.universitymediamonitoringsystem.projection;

public interface TopFiveStatisticProjection {
    String getPostType();

    Integer getPostCount();

    Integer getTotalGrade();

    String getOrganizationName();
}
