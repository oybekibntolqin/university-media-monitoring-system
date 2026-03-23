package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.otfiv.universitymediamonitoringsystem.entity.OnlineBroadcastAndVoiceChatPost;

import java.util.UUID;

public interface OnlineBroadcastAndVoiceChatPostRepository extends JpaRepository<OnlineBroadcastAndVoiceChatPost, UUID> {
}