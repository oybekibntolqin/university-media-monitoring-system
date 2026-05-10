package uz.otfiv.universitymediamonitoringsystem.service.interfaces;

import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.entity.Message;

@Service
public interface MessageService {
    void save(Message message);
}
