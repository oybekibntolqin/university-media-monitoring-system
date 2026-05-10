package uz.otfiv.universitymediamonitoringsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.PostDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.ShowedMedia;
import uz.otfiv.universitymediamonitoringsystem.exception.GlobalExceptionHandler;
import uz.otfiv.universitymediamonitoringsystem.repo.ShowedMediaRepository;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.ShowMediaService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShowMediaServiceImpl implements ShowMediaService {

    private final ShowedMediaRepository showedMediaRepository;
    private final GlobalExceptionHandler globalExceptionHandler;

    @Override
    public void save(ShowedMedia showedMedia) {
        showedMediaRepository.save(showedMedia);
    }

    @Override
    public ShowedMedia findByPostType(String type) {
        ShowedMedia showedMediaByPostType = showedMediaRepository.findShowedMediaByPostType(type);
        System.out.println("type = " + type);
        System.out.println("showedMediaByPostType = " + showedMediaByPostType);
        return showedMediaByPostType;
    }

    @Override
    public ShowedMedia createShowMedia(PostDTO postDTO) {
        try {

            return ShowedMedia.builder()
                    .postType(postDTO.getType())
                    .media(List.of(postDTO.getChannel()))
                    .build();
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return null;
        }
    }

    @Override
    public String findByMediaContaining(String media) {
        Optional<String> byMediaContaining = showedMediaRepository.findByMediaContaining(media);
        return byMediaContaining.orElse(null);
    }

    @Override
    public String findByShowsContaining(String show) {
        return showedMediaRepository.findByShowsContaining(show).orElse(null);
    }

    @Override
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(showedMediaRepository.findAll());
    }

    @Override
    public ResponseEntity<?> getShowedMediaByType(String type) {
        if (type.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (type.startsWith("/")) {
            type = type.substring(1);
        }

        ShowedMedia showedMedia = findByPostType(type);
        return ResponseEntity.ok(showedMedia);
    }
}
