package uz.otfiv.universitymediamonitoringsystem.projection;

import java.util.UUID;

public interface EmployeeWithOrganizationDTO {
    UUID getEmployeeId();

    UUID getId();

    String getFullName();

    String getOrganizationName();

    String getPhone();

    String getEmail();

}
