package uz.otfiv.universitymediamonitoringsystem.service.interfaces;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.BroadcastDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.OnlineBroadcastAndVoiceChatPost;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface BroadcastAndVoiceChatService {
    ResponseEntity<?> createBroadcast(BroadcastDTO broadcastDTO);

    OnlineBroadcastAndVoiceChatPost save(OnlineBroadcastAndVoiceChatPost onlineBroadcastAndVoiceChat);

    ResponseEntity<?> delete(UUID broadcastId);

    ResponseEntity<?> findAll();

    Optional<OnlineBroadcastAndVoiceChatPost> findByChatId(UUID broadcastId);

    ResponseEntity<?> editBroadcast(OnlineBroadcastAndVoiceChatPost onlineBroadcastAndVoiceChat, BroadcastDTO broadcastDTO);

    ResponseEntity<?> getAllByResp();

    List<OnlineBroadcastAndVoiceChatPost> findDeletedBroadcastsByEmployeeId(UUID id);

    Integer getCount();

    List<OnlineBroadcastAndVoiceChatPost> findByEmployeeId(UUID employeeId);

    void absoluteDeleted(UUID id);

    ResponseEntity<?> recovered(UUID broadcastId);

    List<OnlineBroadcastAndVoiceChatPost> findDeletedBroadcastsByAdminId(UUID id);

    List<OnlineBroadcastAndVoiceChatPost> findAdminBroadcasts(UUID adminId);

    List<OnlineBroadcastAndVoiceChatPost> findAllBroadcasts();

    ResponseEntity<?> getALLCount();
}
