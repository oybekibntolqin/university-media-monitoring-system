package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.otfiv.universitymediamonitoringsystem.entity.ShowedMedia;

public interface ShowedMediaRepository extends JpaRepository<ShowedMedia, Integer> {
}