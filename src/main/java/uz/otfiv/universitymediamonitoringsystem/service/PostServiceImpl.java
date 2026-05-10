package uz.otfiv.universitymediamonitoringsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.PostDTO;
import uz.otfiv.universitymediamonitoringsystem.dto.TimeIntervalDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Admin;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.Post;
import uz.otfiv.universitymediamonitoringsystem.entity.ShowedMedia;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Grade;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.Scale;
import uz.otfiv.universitymediamonitoringsystem.exception.GlobalExceptionHandler;
import uz.otfiv.universitymediamonitoringsystem.projection.GetAllPostsProjection;
import uz.otfiv.universitymediamonitoringsystem.repo.PostRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final GlobalExceptionHandler globalExceptionHandler;
    private final EmployeeService employeeService;
    private final MediaEventService mediaEventService;
    private final OfficialPageService officialPageService;
    private final MaterialService materialService;
    private final BroadcastAndVoiceChatService broadcastAndVoiceChatService;
    private final CoverageService coverageService;
    private final MediaProjectService mediaProjectService;
    private final AdminService adminService;
    private final UserService userService;
    private final ShowMediaService showMediaService;
    private final ForeignMaterialService foreignMaterialService;
    private final ExcelService excelService;

    @Override
    public ResponseEntity<?> updatePost(Post post, PostDTO postDTO) {

        try {
            post.setLink(postDTO.getLink());
            post.setDateTime(postDTO.getTime());
            String type = postDTO.getType();

            post.setMedia(postDTO.getShowedMedia());
            post.setShow(postDTO.getChannel());
            post.setPostType(type);

            save(post);
            return ResponseEntity.ok().body(post);
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Override
    public ResponseEntity<?> findAllByPostType(String type) {
        String email = userService.getEmail();

        ResponseEntity<?> resp = employeeService.findByEmail(email);

        if (resp.getStatusCode().value() != 200) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }

        Employee employee = (Employee) resp.getBody();

        List<Post> posts = postRepository.findAllByPostType(type);

        List<Post> postList = posts.stream().filter(post -> {
            assert employee != null;
            return post.getEmployee().getId().equals(employee.getId());
        }).toList();

        return ResponseEntity.ok(postList);
    }

    @Override
    public List<Post> getEmployeePosts(UUID employeeId) {
        return postRepository.findByEmployeeIdAndIsDeletedFalse(employeeId);
    }

    @Override
    public ResponseEntity<?> findAll() {
        String email = userService.getEmail();

        ResponseEntity<?> resp = employeeService.findByEmail(email);
        if (resp.getStatusCode().value() == 200) {

            Employee employee = (Employee) resp.getBody();
            assert employee != null;
            List<GetAllPostsProjection> postsByEmployeeIdAndIsDeletedFalse = postRepository.getAllPostsByEmployeeId(employee.getId());
            return ResponseEntity.ok(postsByEmployeeIdAndIsDeletedFalse);
        } else {

            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();

                List<Post> posts = postRepository.findPostsByAdminIdAndIsDeletedFalse(admin.getId());
                return ResponseEntity.ok(posts);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
    }

    @Override
    public ResponseEntity<?> reloadAllPosts() {

        String email = userService.getEmail();
        Map<String, Object> posts = new HashMap<>();

        ResponseEntity<?> resp = employeeService.findByEmail(email);
        if (resp.getStatusCode().value() == 200) {
            Employee employee = (Employee) resp.getBody();

            assert employee != null;
            posts.put("POSTS", postRepository.findPostsByEmployeeIdAndIsDeletedTrue(employee.getId()));
            posts.put("MEDIA_EVENT", mediaEventService.findDeletedEvents(employee.getId()));
            posts.put("OFFICIAL_PAGE", officialPageService.findDeletedOfficialPagesByEmployeeId(employee.getId()));
            posts.put("MATERIALS", materialService.findDeletedMaterialsByEmployeeId(employee.getId()));
            posts.put("ONLINE_BROADCAST", broadcastAndVoiceChatService.findDeletedBroadcastsByEmployeeId(employee.getId()));
            posts.put("COVERAGES", coverageService.findDeletedByEmployeeId(employee.getId()));
            posts.put("MEDIA_PROJECTS", mediaProjectService.findDeletedByEmployeeId(employee.getId()));
            posts.put("FOREIGN_MATERIAL", foreignMaterialService.findDeletedByEmployeeId(employee.getId()));

            return ResponseEntity.ok(posts);
        } else {
            Optional<Admin> optionalAdmin = adminService.findByEmail(email);
            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();

                posts.put("POSTS", postRepository.findPostsByAdminIdAndIsDeletedTrue(admin.getId()));
                posts.put("MEDIA_EVENT", mediaEventService.findDeletedEventsAdmin(admin.getId()));
                posts.put("OFFICIAL_PAGE", officialPageService.findDeletedOfficialPagesByAdminId(admin.getId()));
                posts.put("MATERIALS", materialService.findDeletedMaterialsByAdminId(admin.getId()));
                posts.put("ONLINE_BROADCAST", broadcastAndVoiceChatService.findDeletedBroadcastsByAdminId(admin.getId()));
                posts.put("COVERAGES", coverageService.findDeletedByAdminId(admin.getId()));
                posts.put("MEDIA_PROJECTS", mediaProjectService.findDeletedByAdminId(admin.getId()));
                posts.put("FOREIGN_MATERIAL", foreignMaterialService.findDeletedByAdminId(admin.getId()));

                return ResponseEntity.ok(posts);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @Override
    public ResponseEntity<?> getAllPosts(UUID employeeId) {
        Map<String, Object> posts = new HashMap<>();
        posts.put("POSTS", postRepository.findPostsByEmployeeIdAndIsDeletedFalse(employeeId));
        posts.put("MEDIA_EVENT", mediaEventService.findByEmployeeId(employeeId));
        posts.put("OFFICIAL_PAGE", officialPageService.findByEmployeeId(employeeId));
        posts.put("MATERIALS", materialService.findByEmployeeId(employeeId));
        posts.put("ONLINE_BROADCAST", broadcastAndVoiceChatService.findByEmployeeId(employeeId));
        posts.put("COVERAGES", coverageService.findByEmployeeId(employeeId));
        posts.put("MEDIA_PROJECTS", mediaProjectService.findByEmployeeId(employeeId));
        posts.put("FOREIGN_MATERIAL", foreignMaterialService.findByEmployeeId(employeeId));
        return ResponseEntity.ok(posts);
    }

    @Override
    public void absoluteDeleted(UUID id) {
        postRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<?> recovered(UUID postId) {
        Optional<Post> optionalPost = findByIdIsDeleted(postId);

        if (optionalPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
        Post post = optionalPost.get();
        post.setIsDeleted(false);
        postRepository.save(post);
        return ResponseEntity.ok(post);
    }

    @Override
    public ResponseEntity<?> getTopFive() {
        return ResponseEntity.ok(postRepository.getTopFive());
    }

    @Override
    public ResponseEntity<?> getAllPostsCount() {
        return ResponseEntity.ok(postRepository.findAllByIsDeletedFalse());
    }

    @Override
    public ResponseEntity<?> getTopFiveQuoter() {
        return ResponseEntity.ok(postRepository.getTopFiveQuoter());
    }

    @Override
    public ResponseEntity<?> createPost(PostDTO postDTO) {
        try {
            if (!(postDTO.getLink().startsWith("http") || postDTO.getLink().startsWith("https"))) {
                return ResponseEntity.badRequest().body("linkni joylayotganingizda http yoki https bilan bo`lishi shart");
            }
            String type = postDTO.getType();

            if (type.startsWith("/")) {
                type = type.substring(1);
            }

            String email = userService.getEmail();
            ResponseEntity<?> res = employeeService.findByEmail(email);
            Employee employee = null;
            Admin admin = null;
            if (res.getStatusCode() == HttpStatus.NOT_FOUND) {
                Optional<Admin> optionalAdmin = adminService.findByEmail(email);
                if (optionalAdmin.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("admin not found");
                }
                admin = optionalAdmin.get();
            } else {
                employee = (Employee) res.getBody();
            }

            ShowedMedia showedMedia = showMediaService.findByPostType(postDTO.getType());
            String containing = showMediaService.findByMediaContaining(postDTO.getChannel());
            if (containing == null) {
                List<String> media = showedMedia.getMedia();
                media.add(postDTO.getChannel());
                showedMedia.setMedia(media);
                showMediaService.save(showedMedia);
            }
            String showed = postDTO.getChannel();
            String byShowsContaining = showMediaService.findByShowsContaining(showed);
            if (byShowsContaining == null) {
                List<String> shows = showedMedia.getShows();
                shows.add(postDTO.getChannel());
                showedMedia.setShows(shows);
                showMediaService.save(showedMedia);
            }
            String scaleFromDTO = postDTO.getScale();
            Scale scale = Scale.valueOf(scaleFromDTO);

            Post post = Post.builder()
                    .dateTime(postDTO.getTime())
                    .link(postDTO.getLink())
                    .show(postDTO.getShowedMedia())
                    .media(postDTO.getChannel())
                    .postType(postDTO.getType())
                    .employee(employee)
                    .stuff(postDTO.getStuff())
                    .admin(admin)
                    .showedUser(postDTO.getShowedUser())
                    .scale(scale)
                    .build();

            if (scale.name().equalsIgnoreCase(Scale.Xorijiy.name())) {
                post.setGrade(Grade.GRADE_TEN);
            } else {
                post.setGrade(Grade.GRADE_FIVE);
            }

            if (employee == null) {
                return ResponseEntity.status(HttpStatus.OK).body(postRepository.save(post));
            }

            if (employee.getRating() == null) {
                employee.setRating(post.getGrade().getValue());
            } else {
                employee.setRating(employee.getRating() + post.getGrade().getValue());
            }

            postRepository.save(post);
            employeeService.save(employee);

            return ResponseEntity.status(HttpStatus.CREATED).body(post);
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private Optional<Post> findByIdIsDeleted(UUID postId) {
        return postRepository.findPostByIdAndIsDeletedTrue(postId);
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public Optional<Post> findById(UUID postId) {
        return postRepository.findPostByIdAndIsDeletedFalse(postId);
    }

    @Override
    public ResponseEntity<?> deletePost(UUID id) {
        try {
            Optional<Post> optionalPost = postRepository.findById(id);
            if (optionalPost.isPresent()) {
                Post post = optionalPost.get();
                post.setIsDeleted(true);
                postRepository.save(post);

                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.internalServerError().build();
        }
    }


    @Override
    public ResponseEntity<?> filterByTime(TimeIntervalDTO timeIntervalDTO) {
        LocalDateTime start = timeIntervalDTO.getStart();
        LocalDateTime end = timeIntervalDTO.getEnd();

        if (start == null || end == null) {
            return ResponseEntity.badRequest().body("Start or end time cannot be null");
        }

        return ResponseEntity.ok(postRepository.findAllByCreatedAtAfterAndCreatedAtBefore(start, end));
    }

    @Override
    public ResponseEntity<?> exportExcelPosts(UUID employeeId) {
        Employee employee = employeeService.findById(employeeId);

        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
        try {

            byte[] excelData = excelService.exportEmployeePosts(employee);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "employee-media-projects.xlsx");
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> exportExcelAdminPosts(Admin admin) {
        try {

            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }

            byte[] excelData = excelService.exportAdminPosts(admin);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "admin-posts.xlsx");
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Override
    public List<Post> findAdminPosts(UUID adminId) {
        return postRepository.findPostsByAdminIdAndIsDeletedFalse(adminId);
    }

    @Override
    public ResponseEntity<?> getTopLastMonthPosts() {
        return ResponseEntity.ok(postRepository.getTopMonthPosts());
    }

    @Override
    public ResponseEntity<?> getPostCountByType() {
        return ResponseEntity.ok(postRepository.getPostCountByType());
    }
}
