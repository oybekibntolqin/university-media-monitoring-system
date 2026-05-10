package uz.otfiv.universitymediamonitoringsystem.service;

import uz.otfiv.universitymediamonitoringsystem.entity.Message;
import uz.otfiv.universitymediamonitoringsystem.repo.MessageRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.MessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }
}
