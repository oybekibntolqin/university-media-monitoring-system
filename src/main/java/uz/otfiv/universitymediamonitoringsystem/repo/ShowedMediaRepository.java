package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.otfiv.universitymediamonitoringsystem.entity.ShowedMedia;

import java.util.Optional;

public interface ShowedMediaRepository extends JpaRepository<ShowedMedia, Integer> {

    ShowedMedia findShowedMediaByPostType(String postType);

    @Query(value = "SELECT smm.media FROM showed_media_media smm " +
            "JOIN showed_media sm ON sm.id = smm.showed_media_id " +
            "WHERE smm.media = :media",
            nativeQuery = true)
    Optional<String> findByMediaContaining(String media);

    @Query(
            value = "SELECT smm.shows FROM showed_media_shows smm " +
                    "JOIN showed_media sm ON sm.id = smm.showed_media_id " +
                    "WHERE smm.shows = ?1",
            nativeQuery = true
    )
    Optional<String> findByShowsContaining(String show);
}