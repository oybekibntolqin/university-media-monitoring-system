package uz.otfiv.universitymediamonitoringsystem.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.otfiv.universitymediamonitoringsystem.dto.PostDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.ShowedMedia;

@Service
public interface ShowMediaService {

    void save(ShowedMedia showedMedia1);

    ShowedMedia findByPostType(String postType);

    ShowedMedia createShowMedia(PostDTO postDTO);

    String  findByMediaContaining(String media);

    String findByShowsContaining(String showedMedia);

    ResponseEntity<?> findAll();

    ResponseEntity<?> getShowedMediaByType(String type);
}
