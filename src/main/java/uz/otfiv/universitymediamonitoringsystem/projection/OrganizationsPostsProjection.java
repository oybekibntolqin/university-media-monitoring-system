package uz.otfiv.universitymediamonitoringsystem.projection;


import uz.otfiv.universitymediamonitoringsystem.entity.enums.Province;

import java.util.UUID;

public interface OrganizationsPostsProjection {
    UUID getId();

    String getProvince();

    String getName();

    Integer getPostCount();

    Integer getTotalGrade();

    default String getLowerCaseProvince() {
        return Province.getStringKey(this.getProvince());
    }
}
