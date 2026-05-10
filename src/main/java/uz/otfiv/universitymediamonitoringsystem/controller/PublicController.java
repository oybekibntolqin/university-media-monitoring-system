package uz.otfiv.universitymediamonitoringsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.otfiv.universitymediamonitoringsystem.projection.GenderProjection;
import uz.otfiv.universitymediamonitoringsystem.projection.OrganizationAndEmployeeInfoProjection;
import uz.otfiv.universitymediamonitoringsystem.projection.OrganizationsPostsProjection;
import uz.otfiv.universitymediamonitoringsystem.service.ForeignMaterialServiceImpl;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {
    private final EmployeeService employeeService;
    private final PostService postService;
    private final OrganizationService organizationService;
    private final MediaEventService mediaEventService;
    private final BroadcastAndVoiceChatService broadcastAndVoiceChatService;
    private final MaterialService materialService;
    private final ForeignMaterialServiceImpl foreignMaterialService;


    @GetMapping("/get-top-five")
    public ResponseEntity<?> getTopFive() {
        return postService.getTopFive();
    }

    @GetMapping("/get-mediaEvent-count")
    public ResponseEntity<?> getMediaEvenCount() {
        return mediaEventService.getMediaEventCount(); // hozirga
    }

    @GetMapping("/get-material-count")
    public ResponseEntity<?> getMaterialEvenCount() {
        return materialService.getMaterialCount(); // hozirga
    }

    @GetMapping("/get-broadcast-count")
    public ResponseEntity<?> getBroadcastEvenCount() {
        return broadcastAndVoiceChatService.getALLCount(); // hozirga
    }

    @GetMapping("/get-foreign-count")
    public ResponseEntity<?> getForeignEvenCount() {
        return foreignMaterialService.getAllCount(); // hozirga
    }

    @GetMapping("/get-posts-by-type")
    public ResponseEntity<?> getPostsByType() {
        return postService.getPostCountByType(); // hozirga
    }

    @GetMapping("/genders-by-region")
    public ResponseEntity<?> gendersByRegion() {
        List<GenderProjection> genderDTOS = employeeService.filterByProvince();
        return ResponseEntity.ok(genderDTOS);
    }

    @GetMapping("/genders-by-republic")
    public ResponseEntity<?> gendersByRepublic() {
        return ResponseEntity.ok(employeeService.getEmployeeByGenderRepublic());
    }

    @GetMapping("/employee-posts")
    public ResponseEntity<?> userPosts(@RequestParam String postType) {
        if (postType.isBlank()) {
            return ResponseEntity.badRequest().body("Invalid post type");
        }
        return postService.findAllByPostType(postType);
    }


    @GetMapping("/organization-employee")
    public ResponseEntity<?> getOrganizationEmployee() {
        List<OrganizationAndEmployeeInfoProjection> employees = organizationService.getOrganizationTheEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employee-info")
    public ResponseEntity<?> getEmployeeInfo(@RequestParam UUID employeeId) {
        return employeeService.getEmployeeInfo(employeeId);
    }

    @GetMapping("/top-10")
    public ResponseEntity<?> getTop10Employees() {
        return employeeService.getTop(10);
    }

    @GetMapping("/media-event-count")
    public ResponseEntity<?> getMediaEventCount() {
        return ResponseEntity.ok(mediaEventService.getCount());
    }

    @GetMapping("/online-broadcasts-count")
    public ResponseEntity<?> getBroadcastsCount() {
        return ResponseEntity.ok(broadcastAndVoiceChatService.getCount());
    }


    @GetMapping("/universities-by-province")
    public ResponseEntity<?> getUniversitiesByProvinces() {
        return organizationService.getUniversitiesByProvince();
    }

    @GetMapping("/top-universities-by-province")
    public ResponseEntity<List<OrganizationsPostsProjection>> getTopUniversitiesByProvince(@RequestParam String province) {
        return organizationService.getTopUniversitiesByProvince(province);
    }

    @GetMapping("/university-posts-count")
    public ResponseEntity<?> getUniversityPostsCount(@RequestParam UUID organizationId) {
        System.out.println("organizationId = " + organizationId);
        return organizationService.getOrganizationPosts(organizationId);
    }


    @GetMapping("/all-posts-count")
    public ResponseEntity<?> getAllPostsCount() {
        return postService.getAllPostsCount();
    }


    @GetMapping("/universities-rating-by-posts")
    public ResponseEntity<?> getUniversityRankingsByPosts() {
        return organizationService.getRatingAllUniversities();
    }

    @GetMapping("/top-last-month-posts")
    public ResponseEntity<?> getTopLastMonthPosts() {
        return ResponseEntity.ok(postService.getTopLastMonthPosts());
    }

    @GetMapping("/get-top-quoter")
    public ResponseEntity<?> getTopQuoter() {
        return postService.getTopFiveQuoter();
    }
}
