package uz.otfiv.universitymediamonitoringsystem.projection;


import uz.otfiv.universitymediamonitoringsystem.entity.Employee;

public interface TopRatingEmployeesProjection {
    Employee getEmployee();
    Integer getRating();
}
