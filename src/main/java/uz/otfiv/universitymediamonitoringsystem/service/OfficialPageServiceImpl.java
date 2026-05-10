package uz.otfiv.universitymediamonitoringsystem.service;

import com.google.api.client.json.gson.GsonFactory;
import uz.otfiv.universitymediamonitoringsystem.dto.OfficialPageDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Admin;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.OfficialPage;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Messenger;
import uz.otfiv.universitymediamonitoringsystem.exception.GlobalExceptionHandler;
import uz.otfiv.universitymediamonitoringsystem.repo.OfficialPageRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.AdminService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.EmployeeService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.OfficialPageService;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.UserService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetChatMembersCount;
import com.pengrad.telegrambot.response.GetChatMembersCountResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OfficialPageServiceImpl implements OfficialPageService {
    private final OfficialPageRepository officialPageRepository;
    private final GlobalExceptionHandler globalExceptionHandler;
    private final EmployeeService employeeService;
    private final TelegramBot bot;
    private final AdminService adminService;

    private static final String API_KEY = "AIzaSyA9DFBoon833KKk9TS4cvxIoJABEJBmfPg"; // YouTube API kalitini kiriting
    private static final String APPLICATION_NAME = "youtube-subscriber-count";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final UserService userService;

    @Override
    public OfficialPage save(OfficialPage officialPage) {

        officialPage.setFollowers(getCountFollowersBySocial(officialPage.getMessengersAndLinks()));

        return officialPageRepository.save(officialPage);
    }

    private Map<Messenger, BigInteger> getCountFollowersBySocial(Map<Messenger, String> socialMedia) {

        Map<Messenger, BigInteger> followers = new HashMap<>();

        for (Map.Entry<Messenger, String> entry : socialMedia.entrySet()) {
            if (entry.getKey() == Messenger.YOUTUBE) {
                followers.put(Messenger.YOUTUBE, getAmountFollowersByYouTube(entry.getValue()));
            } else if (entry.getKey() == Messenger.TELEGRAM) {
                followers.put(Messenger.TELEGRAM, getAmountFollowersByTelegram(entry.getValue()));
            } else if (entry.getKey() == Messenger.INSTAGRAM) {
                // todo must be method of getting instagram followers
            }
        }

        return followers;
    }

    private BigInteger getAmountFollowersByTelegram(String link) {

        String[] parts = link.split("/");

        // Sleshdan keyingi so'zni olish
        String channelUser = "@" + parts[parts.length - 1];

// Create the request to get the members count
        GetChatMembersCount countRequest = new GetChatMembersCount(channelUser);

        // Execute the request and get the response
        GetChatMembersCountResponse response = bot.execute(countRequest);

        if (response.isOk()) {
            int count = response.count(); // Retrieve the member count
            System.out.println("Channel subscriber count: " + count);
            return BigInteger.valueOf(count);
        } else {
            System.out.println("Error: " + response.description()); // Print the error description
        }
        return BigInteger.ZERO;
    }

    private BigInteger getAmountFollowersByYouTube(String link) {

        String[] parts = link.split("/");

        // Sleshdan keyingi so'zni olish
        String channelName = parts[parts.length - 1];

        String channelId = getChannelId(channelName);

        if (channelId != null) {
            return getSubscriberCount(channelId);
        }
        return BigInteger.ZERO;
    }

    @SneakyThrows
    private BigInteger getSubscriberCount(String channelId) {

        YouTube youtubeService = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();

        YouTube.Channels.List request = youtubeService.channels()
                .list("statistics")
                .setKey(API_KEY)
                .setId(channelId); // Kanal ID sini foydalanadi

        ChannelListResponse response = request.execute();
        List<Channel> channels = response.getItems();

        if (channels != null && !channels.isEmpty()) {
            Channel channel = channels.get(0);
            return channel.getStatistics().getSubscriberCount();

        }
        return BigInteger.ZERO;
    }

    @SneakyThrows
    private String getChannelId(String channelName) {
        YouTube youtubeService = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();

        YouTube.Search.List request = youtubeService.search()
                .list("id")
                .setQ(channelName)
                .setType("channel")
                .setKey(API_KEY)
                .setMaxResults(1L);

        SearchListResponse response = request.execute();
        List<SearchResult> results = response.getItems();

        if (!results.isEmpty()) {
            return results.get(0).getId().getChannelId(); // Kanal ID sini qaytaradi
        }
        return null;
    }


    @Override
    public Optional<OfficialPage> findById(UUID officialPageId) {
        return officialPageRepository.findById(officialPageId);
    }

    @Override
    public ResponseEntity<?> updateOfficialPage(OfficialPage officialPage, OfficialPageDTO officialPageDTO) {
        try {

            Map<Messenger, String> map = new HashMap<>();
            map.put(Messenger.TELEGRAM, officialPageDTO.getTelegramLink());
            map.put(Messenger.X, officialPageDTO.getLink());
            map.put(Messenger.YOUTUBE, officialPageDTO.getYoutubeLink());
            map.put(Messenger.INSTAGRAM, officialPageDTO.getInstagramLink());
            officialPage.setMessengersAndLinks(map);
            save(officialPage);
            return ResponseEntity.ok().body(officialPage);
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> delete(UUID officialPageId) {
        Optional<OfficialPage> optionalPage = findById(officialPageId);
        if (optionalPage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        OfficialPage officialPage = optionalPage.get();
        officialPage.setIsDeleted(true);
        save(officialPage);
        return ResponseEntity.ok().build();
    }

    @Override
    public boolean hasOfficialPages(String type) {

        Messenger messenger = Messenger.fromValue(type);

        String email = userService.getEmail();
        ResponseEntity<?> byEmail = employeeService.findByEmail(email);
        if (byEmail.getStatusCode().value() != 200) {
            return false;
        }
        Employee employee = (Employee) byEmail.getBody();

        List<OfficialPage> allByEmployee = officialPageRepository.findOfficialPagesByEmployeeAndIsDeletedFalse(employee);
        return !allByEmployee.isEmpty();
    }

    @Override
    public ResponseEntity<?> findByMessengerAndEmployeeId(OfficialPageDTO officialPageDTO) {
        String email = userService.getEmail();
        ResponseEntity<?> byEmail = employeeService.findByEmail(email);
        if (byEmail.getStatusCode().value() != 200) {
            return ResponseEntity.badRequest().body(byEmail.getBody());
        }
        Employee employee = (Employee) byEmail.getBody();
        if (employee == null) {
            return ResponseEntity.badRequest().body("Employee not found in findByMessengerAndEmployeeId()");
        }


        Optional<OfficialPage> optionOfficialPage = Optional.empty();

        for (Messenger messenger : Messenger.values()) {

            optionOfficialPage = officialPageRepository.findOfficialPageByMessengers(messenger.name(), employee.getId());
            break;
        }

        if (optionOfficialPage.isEmpty()) {
            OfficialPage newOfficialPage = new OfficialPage();


            Map<Messenger, String> map = new HashMap<>();
            map.put(Messenger.TELEGRAM, officialPageDTO.getTelegramLink());
            map.put(Messenger.X, officialPageDTO.getLink());
            map.put(Messenger.YOUTUBE, officialPageDTO.getYoutubeLink());
            map.put(Messenger.INSTAGRAM, officialPageDTO.getInstagramLink());


            newOfficialPage.setMessengersAndLinks(map);
            newOfficialPage.setEmployee(employee);
            newOfficialPage.setIsDeleted(false);

            OfficialPage save = officialPageRepository.save(newOfficialPage);

            return ResponseEntity.ok().body(save);
        }
        return ResponseEntity.ok().body(optionOfficialPage.get());

    }

    @Override
    public ResponseEntity<?> findAll() {

        String email = userService.getEmail();

        ResponseEntity<?> byEmail = employeeService.findByEmail(email);
        if (byEmail.getStatusCode().value() == 200) {
            Employee employee = (Employee) byEmail.getBody();
            if (employee == null) {
                return ResponseEntity.badRequest().body("Employee not found in findByMessengerAndEmployeeId()");
            } else {
                return ResponseEntity.ok(officialPageRepository.findOfficialPagesByEmployeeIdAndIsDeletedFalse(employee.getId()));
            }
        } else {
            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();
                return ResponseEntity.ok(officialPageRepository.findOfficialPagesByAdminIdAndIsDeletedFalse(admin.getId()));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public List<OfficialPage> findDeletedOfficialPagesByEmployeeId(UUID employeeId) {
        return officialPageRepository.findOfficialPagesByEmployeeIdAndIsDeletedTrue(employeeId);
    }

    @Override
    public List<OfficialPage> findByEmployeeId(UUID employeeId) {
        return officialPageRepository.findOfficialPagesByEmployeeIdAndIsDeletedFalse(employeeId);
    }

    @Override
    public void absoluteDeleted(UUID id) {
        officialPageRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<?> recovered(UUID officialPageId) {
        Optional<OfficialPage> optionalOfficialPage = findById(officialPageId);

        if (optionalOfficialPage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        optionalOfficialPage.get().setIsDeleted(false);
        save(optionalOfficialPage.get());
        return ResponseEntity.ok().build();
    }

    @Override
    public List<OfficialPage> findDeletedOfficialPagesByAdminId(UUID adminId) {
        return officialPageRepository.findOfficialPagesByAdminIdAndIsDeletedTrue(adminId);
    }

    @Override
    public ResponseEntity<?> create(OfficialPageDTO officialPageDTO) {
        String email = userService.getEmail();
        ResponseEntity<?> resp = employeeService.findByEmail(email);
        OfficialPage officialPage = new OfficialPage();

        Map<Messenger, String> map = new HashMap<>();
        map.put(Messenger.TELEGRAM, officialPageDTO.getTelegramLink());
        map.put(Messenger.X, officialPageDTO.getLink());
        map.put(Messenger.YOUTUBE, officialPageDTO.getYoutubeLink());
        map.put(Messenger.INSTAGRAM, officialPageDTO.getInstagramLink());

        officialPage.setMessengersAndLinks(map);

        if (resp.getStatusCode().value() == 200) {
            Employee employee = (Employee) resp.getBody();
            officialPage.setEmployee(employee);
            return ResponseEntity.ok(save(officialPage));
        } else {
            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();
                officialPage.setAdmin(admin);
            }else {
                return ResponseEntity.badRequest().body("User not found");
            }
            return ResponseEntity.status(201).body(save(officialPage));
        }

    }
}
