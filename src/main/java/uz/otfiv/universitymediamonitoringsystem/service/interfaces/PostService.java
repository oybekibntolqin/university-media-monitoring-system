package uz.otfiv.universitymediamonitoringsystem.service.interfaces;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.PostDTO;
import uz.otfiv.universitymediamonitoringsystem.dto.TimeIntervalDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Admin;
import uz.otfiv.universitymediamonitoringsystem.entity.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface PostService {
    void save(Post post);

    Optional<Post> findById(UUID id);

    ResponseEntity<?> deletePost(UUID id);

    ResponseEntity<?> createPost(PostDTO postDTO);

    ResponseEntity<?> updatePost(Post post, PostDTO postDTO);

    ResponseEntity<?> filterByTime(@NotNull TimeIntervalDTO timeIntervalDTO);

    ResponseEntity<?> findAllByPostType(String type);

    List<Post> getEmployeePosts(UUID id);

    ResponseEntity<?> findAll();

    ResponseEntity<?> reloadAllPosts();

    ResponseEntity<?> exportExcelPosts(UUID employeeId);

    ResponseEntity<?> exportExcelAdminPosts(Admin admin);

    List<Post> findAdminPosts(UUID id);

    ResponseEntity<?> getAllPosts(UUID employeeId);

    void absoluteDeleted(UUID id);

    ResponseEntity<?> recovered(UUID postId);

    ResponseEntity<?> getTopFive();

    ResponseEntity<?> getAllPostsCount();

    ResponseEntity<?> getTopFiveQuoter();

    ResponseEntity<?> getTopLastMonthPosts();

    ResponseEntity<?> getPostCountByType();
}
