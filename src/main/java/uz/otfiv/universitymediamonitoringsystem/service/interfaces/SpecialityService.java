package uz.otfiv.universitymediamonitoringsystem.service.interfaces;

import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.entity.Speciality;

@Service
public interface SpecialityService {
    Speciality save(Speciality speciality);

}
