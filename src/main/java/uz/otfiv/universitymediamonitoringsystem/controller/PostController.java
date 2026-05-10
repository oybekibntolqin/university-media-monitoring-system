package uz.otfiv.universitymediamonitoringsystem.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.otfiv.universitymediamonitoringsystem.dto.PostDTO;
import uz.otfiv.universitymediamonitoringsystem.dto.TimeIntervalDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.Post;
import uz.otfiv.universitymediamonitoringsystem.entity.ShowedMedia;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.MaterialType;
import uz.otfiv.universitymediamonitoringsystem.entity.enums.MediaEventType;
import uz.otfiv.universitymediamonitoringsystem.exception.GlobalExceptionHandler;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final ShowMediaService showMediaService;
    private final PostService postService;
    private final GlobalExceptionHandler globalExceptionHandler;
    private final BroadcastAndVoiceChatService broadcastAndVoiceChatService;
    private final MediaEventService mediaEventService;
    private final MaterialService materialService;

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return postService.findAll();
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/all-reload")
    public ResponseEntity<?> getAllReload() {
        return postService.reloadAllPosts();
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/recovered")
    public ResponseEntity<?> getRecovered(@RequestParam UUID postId) {
        return postService.recovered(postId);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/get-broadcast-posts")
    public ResponseEntity<?> getBroadcastPosts() {
        return broadcastAndVoiceChatService.getAllByResp();
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/get-posts-by-types")
    public ResponseEntity<?> getPostsByType(@RequestParam String type) {
        if (type.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return postService.findAllByPostType(type);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all-posts")
    public ResponseEntity<?> getAllPosts(@RequestParam UUID employeeId) {
        return postService.getAllPosts(employeeId);
    }


    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/get-media-events")
    public ResponseEntity<?> getMediaEvents(@RequestParam String type) {
        if (type.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        MediaEventType mediaEventType = MediaEventType.valueOf(type);
        return mediaEventService.findAll(mediaEventType);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/get-material-posts")
    public ResponseEntity<?> getMaterialPosts(@RequestParam String type) {
        if (type.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        MaterialType materialType = MaterialType.fromValue(type);
        return materialService.getAll(materialType);
    }


    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @PostMapping("/get-content")
    public ResponseEntity<?> getType(@RequestParam String type) {
        return showMediaService.getShowedMediaByType(type);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PostDTO postDTO) {
        try {
            return postService.createPost(postDTO);
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/selected-type")
    public ResponseEntity<?> createSelectedType(@RequestParam String type) {
        ShowedMedia showedMedia = showMediaService.findByPostType(type);
        return ResponseEntity.ok().body(showedMedia);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam UUID postId, @RequestBody PostDTO postDTO) {
        try {
            Optional<Post> optionalPost = postService.findById(postId);
            if (optionalPost.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Post post = optionalPost.get();
            ResponseEntity<?> updatedPost = postService.updatePost(post, postDTO);
            return ResponseEntity.ok().body(updatedPost);
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam UUID id) {
        return postService.deletePost(id);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/search-by-time")
    public ResponseEntity<?> search(@RequestBody @NotNull TimeIntervalDTO timeIntervalDTO) {
        return postService.filterByTime(timeIntervalDTO);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @DeleteMapping("/absolute-delete")
    public ResponseEntity<?> absoluteDelete(@RequestParam UUID postId) {
        postService.absoluteDeleted(postId);
        return ResponseEntity.ok().build();
    }
}