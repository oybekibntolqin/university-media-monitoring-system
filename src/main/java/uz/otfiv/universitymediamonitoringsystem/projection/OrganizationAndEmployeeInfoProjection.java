package uz.otfiv.universitymediamonitoringsystem.projection;

import java.util.UUID;

public interface OrganizationAndEmployeeInfoProjection {
    UUID getEmployeeId();

    UUID getOrganizationId();

    String getEmployeeFullName();

    String getOrganizationName();

    Integer getOrganizationCount();

    String getProvince();

    byte[] getImage();


}
