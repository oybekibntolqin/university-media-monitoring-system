package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.otfiv.universitymediamonitoringsystem.entity.OfficialPage;

import java.util.UUID;

public interface OfficialPageRepository extends JpaRepository<OfficialPage, UUID> {
}