package uz.otfiv.universitymediamonitoringsystem.service;

import uz.otfiv.universitymediamonitoringsystem.dto.BroadcastDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Admin;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.OnlineBroadcastAndVoiceChatPost;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Messenger;
import uz.otfiv.universitymediamonitoringsystem.repo.OnlineBroadcastAndVoiceChatRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.AdminService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.BroadcastAndVoiceChatService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.EmployeeService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BroadcastAndVoiceChatServiceImpl implements BroadcastAndVoiceChatService {
    private final EmployeeService employeeService;
    private final OnlineBroadcastAndVoiceChatRepository onlineBroadcastAndVoiceChatRepository;
    private final AdminService adminService;
    private final UserService userService;

    @Override
    public ResponseEntity<?> createBroadcast(BroadcastDTO broadcastDTO) {
        String email = userService.getEmail();

        ResponseEntity<Object> BAD_REQUEST = getObjectResponseEntity(email);
        if (BAD_REQUEST != null) return BAD_REQUEST;

        boolean isInvalidMessengers = isInvalidMessengers(broadcastDTO);

        if (isInvalidMessengers) {
            return ResponseEntity.badRequest().body(broadcastDTO.getMessenger());
        }

        ResponseEntity<?> resp = employeeService.findByEmail(email);
        Optional<Admin> optionAdmin = adminService.findByEmail(email);

        OnlineBroadcastAndVoiceChatPost onlineBroadcastAndVoiceChatPost = new OnlineBroadcastAndVoiceChatPost();
        onlineBroadcastAndVoiceChatPost.setGrade(Grade.GRADE_FIVE);

        if (resp.getStatusCode().value() == 200) {
            Employee employee = (Employee) resp.getBody();

            onlineBroadcastAndVoiceChatPost.setEmployee(employee);

            assert employee != null;
            if (employee.getRating() != null) {
                employee.setRating(employee.getRating() + onlineBroadcastAndVoiceChatPost.getGrade().getValue());
            } else {
                employee.setRating(onlineBroadcastAndVoiceChatPost.getGrade().getValue());
            }
            employeeService.save(employee);
        } else if (optionAdmin.isPresent()) {
            onlineBroadcastAndVoiceChatPost.setAdmin(optionAdmin.get());
        } else {
            return ResponseEntity.badRequest().body("Not found User");
        }

        System.out.println("broadcastDTO.getStuffs() = " + broadcastDTO.getStuffs());

        onlineBroadcastAndVoiceChatPost.setTitle(broadcastDTO.getTitle());
        onlineBroadcastAndVoiceChatPost.setStuff(broadcastDTO.getStuffs());
        onlineBroadcastAndVoiceChatPost.setLink(broadcastDTO.getLink());
        onlineBroadcastAndVoiceChatPost.setEventDate(broadcastDTO.getEventDate());
        onlineBroadcastAndVoiceChatPost.setMessenger(Messenger.fromValue(broadcastDTO.getMessenger()));
        onlineBroadcastAndVoiceChatPost.setNumberOfPeople(broadcastDTO.getAmount());

        save(onlineBroadcastAndVoiceChatPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(onlineBroadcastAndVoiceChatPost);
    }

    @Nullable
    private static ResponseEntity<Object> getObjectResponseEntity(String email) {
        if (email == null || email.isBlank() || email.equals("anonymous")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Override
    public OnlineBroadcastAndVoiceChatPost save(OnlineBroadcastAndVoiceChatPost onlineBroadcastAndVoiceChat) {
        return onlineBroadcastAndVoiceChatRepository.save(onlineBroadcastAndVoiceChat);
    }

    @Override
    public ResponseEntity<?> delete(UUID broadcastId) {
        onlineBroadcastAndVoiceChatRepository.deleteBroadCast(broadcastId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<?> findAll() {

        String email = userService.getEmail();

        ResponseEntity<?> resp = employeeService.findByEmail(email);

        if (resp.getStatusCode().value() == 200) {
            Employee employee = (Employee) resp.getBody();

            assert employee != null;
            return ResponseEntity.ok(onlineBroadcastAndVoiceChatRepository.findAllByEmployeeId(employee.getId()));
        } else {
            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();
                return ResponseEntity.ok(onlineBroadcastAndVoiceChatRepository.findOnlineBroadcastAndVoiceChatPostsByAdminIdAndIsDeletedFalse(admin.getId()));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public Optional<OnlineBroadcastAndVoiceChatPost> findByChatId(UUID broadcastId) {
        return onlineBroadcastAndVoiceChatRepository.findById(broadcastId);
    }

    @Override
    public ResponseEntity<?> editBroadcast(OnlineBroadcastAndVoiceChatPost onlineBroadcastAndVoiceChat, BroadcastDTO broadcastDTO) {
        boolean invalidMessengers = isInvalidMessengers(broadcastDTO);

        if (invalidMessengers) {
            return ResponseEntity.badRequest().body(broadcastDTO.getMessenger());
        }
        if (broadcastDTO.getAmount() <= 0) {
            return ResponseEntity.badRequest().body(broadcastDTO.getAmount());
        }

        onlineBroadcastAndVoiceChat.setLink(broadcastDTO.getLink());
        onlineBroadcastAndVoiceChat.setEventDate(broadcastDTO.getEventDate());
        onlineBroadcastAndVoiceChat.setStuff(broadcastDTO.getStuffs());
        onlineBroadcastAndVoiceChat.setMessenger(Messenger.fromValue(broadcastDTO.getMessenger()));
        onlineBroadcastAndVoiceChat.setNumberOfPeople(broadcastDTO.getAmount());
        onlineBroadcastAndVoiceChat.setTitle(broadcastDTO.getTitle());
        save(onlineBroadcastAndVoiceChat);

        return ResponseEntity.ok(onlineBroadcastAndVoiceChat);
    }

    @Override
    public ResponseEntity<?> getAllByResp() {
        String email = userService.getEmail();
        ResponseEntity<Object> badRequest = getObjectResponseEntity(email);

        if (badRequest != null) return badRequest;
        ResponseEntity<?> resp = employeeService.findByEmail(email);

        if (resp.getStatusCode().value() != 200) {
            return ResponseEntity.notFound().build();
        }

        Employee employee = (Employee) resp.getBody();
        assert employee != null;
        List<OnlineBroadcastAndVoiceChatPost> posts = findByEmployeeId(employee.getId());
        return ResponseEntity.ok(posts);
    }

    @Override
    public List<OnlineBroadcastAndVoiceChatPost> findDeletedBroadcastsByEmployeeId(UUID employeeId) {
        return onlineBroadcastAndVoiceChatRepository.findOnlineBroadcastAndVoiceChatPostsByEmployeeIdAndIsDeletedTrue(employeeId);
    }

    @Override
    public Integer getCount() {
        return onlineBroadcastAndVoiceChatRepository.countOnlineBroadcastAndVoiceChatPostsByIsDeletedTrue();
    }

    public List<OnlineBroadcastAndVoiceChatPost> findByEmployeeId(UUID id) {
        return onlineBroadcastAndVoiceChatRepository.findAllByEmployeeIdAndIsDeletedFalse(id);
    }

    @Override
    public void absoluteDeleted(UUID id) {
        onlineBroadcastAndVoiceChatRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<?> recovered(UUID broadcastId) {
        Optional<OnlineBroadcastAndVoiceChatPost> postOptional = findByChatId(broadcastId);
        if (postOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(broadcastId);
        }
        OnlineBroadcastAndVoiceChatPost onlineBroadcastAndVoiceChatPost = postOptional.get();
        onlineBroadcastAndVoiceChatPost.setIsDeleted(false);
        onlineBroadcastAndVoiceChatRepository.save(onlineBroadcastAndVoiceChatPost);
        return ResponseEntity.ok(onlineBroadcastAndVoiceChatPost);
    }

    @Override
    public List<OnlineBroadcastAndVoiceChatPost> findDeletedBroadcastsByAdminId(UUID adminId) {
        return onlineBroadcastAndVoiceChatRepository.findOnlineBroadcastAndVoiceChatPostsByAdminIdAndIsDeletedTrue(adminId);
    }

    private static boolean isInvalidMessengers(BroadcastDTO broadcastDTO) {
        return Arrays.stream(Messenger.values())
                .noneMatch(messengers -> messengers.toString().equalsIgnoreCase(broadcastDTO.getMessenger()));
    }


    @Override
    public List<OnlineBroadcastAndVoiceChatPost> findAdminBroadcasts(UUID adminId) {
        return onlineBroadcastAndVoiceChatRepository.findOnlineBroadcastAndVoiceChatPostsByAdminIdAndIsDeletedFalse(adminId);
    }

    @Override
    public List<OnlineBroadcastAndVoiceChatPost> findAllBroadcasts() {
        return onlineBroadcastAndVoiceChatRepository.findAllByIsDeletedFalse();
    }

    @Override
    public ResponseEntity<?> getALLCount() {
        return ResponseEntity.ok(onlineBroadcastAndVoiceChatRepository.countOnlineBroadcastAndVoiceChatPostsByIsDeletedFalse());
    }
}
