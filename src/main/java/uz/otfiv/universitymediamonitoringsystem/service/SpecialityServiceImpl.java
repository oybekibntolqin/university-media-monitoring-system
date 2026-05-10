package uz.otfiv.universitymediamonitoringsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.entity.Speciality;
import uz.otfiv.universitymediamonitoringsystem.repo.SpecialityRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.SpecialityService;

@Service
@RequiredArgsConstructor
public class SpecialityServiceImpl implements SpecialityService {
    private final SpecialityRepository specialityRepository;

    @Override
    public Speciality save(Speciality speciality) {
        return specialityRepository.save(speciality);
    }
}
