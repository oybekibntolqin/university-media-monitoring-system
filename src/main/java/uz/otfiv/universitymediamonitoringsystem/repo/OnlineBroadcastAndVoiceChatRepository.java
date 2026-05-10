package uz.otfiv.universitymediamonitoringsystem.repo;

import uz.otfiv.universitymediamonitoringsystem.entity.OnlineBroadcastAndVoiceChatPost;
import uz.otfiv.universitymediamonitoringsystem.projection.GetAllBroadcasts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface OnlineBroadcastAndVoiceChatRepository extends JpaRepository<OnlineBroadcastAndVoiceChatPost, UUID> {
    @Modifying
    @Transactional
    @Query(value = "update online_broadcast_and_voice_chat_post set is_deleted = true where id = ?1", nativeQuery = true)
    void deleteBroadCast(UUID id);

    List<OnlineBroadcastAndVoiceChatPost> findAllByEmployeeIdAndIsDeletedFalse(UUID employeeId);

    List<OnlineBroadcastAndVoiceChatPost> findOnlineBroadcastAndVoiceChatPostsByEmployeeIdAndIsDeletedTrue(UUID employeeId);

    List<OnlineBroadcastAndVoiceChatPost> findOnlineBroadcastAndVoiceChatPostsByEmployeeIdAndIsDeletedFalse(UUID id);

    Integer countOnlineBroadcastAndVoiceChatPostsByIsDeletedTrue();

    List<OnlineBroadcastAndVoiceChatPost> findOnlineBroadcastAndVoiceChatPostsByAdminIdAndIsDeletedFalse(UUID id);

    List<OnlineBroadcastAndVoiceChatPost> findOnlineBroadcastAndVoiceChatPostsByAdminIdAndIsDeletedTrue(UUID adminId);

    List<OnlineBroadcastAndVoiceChatPost> findAllByIsDeletedFalse();

    @Query(value = """
            select b.id, b.title, b.event_date, s.stuff, b.messenger, b.number_of_people, b.link, b.grade
            from online_broadcast_and_voice_chat_post b inner join online_broadcast_and_voice_chat_post_stuff s on s.online_broadcast_and_voice_chat_post_id = b.id
            where b.is_deleted = false and b.employee_id = ?1
            """, nativeQuery = true)
    List<GetAllBroadcasts> findAllByEmployeeId(UUID employeeId);

    Integer countOnlineBroadcastAndVoiceChatPostsByIsDeletedFalse();
}