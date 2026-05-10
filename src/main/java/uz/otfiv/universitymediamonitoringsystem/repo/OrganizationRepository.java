package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.otfiv.universitymediamonitoringsystem.entity.Organization;
import uz.otfiv.universitymediamonitoringsystem.projection.OrganizationAllPostsCountProjection;
import uz.otfiv.universitymediamonitoringsystem.projection.OrganizationAndEmployeeInfoProjection;
import uz.otfiv.universitymediamonitoringsystem.projection.OrganizationsPostsProjection;
import uz.otfiv.universitymediamonitoringsystem.projection.ProvinceCountProjection;

import java.util.List;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    @Query(value = """
            select e.id as employee_id, o.id as organization_id, u.full_name as employee_full_name, o.name as organization_name, COUNT(o.id) as organization_count, e.province, at.content as image 
            from users u join organization o on u.organization_id = o.id join
                employee e on o.id = e.user_id join 
                attachment_content at on at.id = u.attachment_content_id 
                                      group by e.id, o.id, u.full_name, o.name, e.province, at.content
            """, nativeQuery = true)
    List<OrganizationAndEmployeeInfoProjection> getOrganizationTheEmployees();

    @Query(value = """
            SELECT province, count(*) AS institute_count
            FROM organization
            GROUP BY province
            ORDER BY province;
            """, nativeQuery = true)
    List<ProvinceCountProjection> findInstituteCountByProvince();

    @Query(value = """
            SELECT o.id,
                   o.name,
                   o.province,
                   COALESCE(SUM(p_count), 0) + COALESCE(SUM(m_count), 0) + COALESCE(SUM(c_count), 0) +
                   COALESCE(SUM(mp_count), 0) + COALESCE(SUM(mat_count), 0) + COALESCE(SUM(ob_count), 0) + COALESCE(SUM(f_count), 0) AS post_count
            FROM organization o
                     LEFT JOIN users u ON o.id = u.organization_id
                     LEFT JOIN employee e ON u.id = e.user_id
                     LEFT JOIN (
                         SELECT employee_id, COUNT(id) AS p_count
                         FROM post
                         WHERE is_deleted = false
                         GROUP BY employee_id
                     ) p ON e.id = p.employee_id
                     LEFT JOIN (
                         SELECT employee_id, COUNT(id) AS m_count
                         FROM media_event
                         WHERE is_deleted = false
                         GROUP BY employee_id
                     ) m ON e.id = m.employee_id
                     LEFT JOIN (
                         SELECT employee_id, COUNT(id) AS c_count
                         FROM coverage
                         WHERE is_deleted = false
                         GROUP BY employee_id
                     ) c ON e.id = c.employee_id
                     LEFT JOIN (
                         SELECT employee_id, COUNT(id) AS mp_count
                         FROM media_project
                         WHERE is_deleted = false
                         GROUP BY employee_id
                     ) mp ON e.id = mp.employee_id
                     LEFT JOIN (
                         SELECT employee_id, COUNT(id) AS mat_count
                         FROM material
                         WHERE is_deleted = false
                         GROUP BY employee_id
                     ) mat ON e.id = mat.employee_id
                     LEFT JOIN (
                         SELECT employee_id, COUNT(id) AS ob_count
                         FROM online_broadcast_and_voice_chat_post
                         WHERE is_deleted = false
                         GROUP BY employee_id
                     ) ob ON e.id = ob.employee_id
                     LEFT JOIN (
                         SELECT employee_id, COUNT(id) AS f_count
                         FROM foreign_materials
                         WHERE is_deleted = false
                         GROUP BY employee_id
                     ) f ON e.id = f.employee_id
            WHERE o.province = ?1
            GROUP BY o.id, o.name
            ORDER BY post_count DESC;
            
            """, nativeQuery = true)
    List<OrganizationsPostsProjection> getTopOrganizationsByProvince(String province);

    @Query(value = """
                       SELECT
                           o.name as name,
                           COUNT(DISTINCT p.id) AS posts_count,
                           COUNT(DISTINCT me.id) AS media_events_count,
                           COUNT(DISTINCT c.id) AS coverages_count,
                           COUNT(DISTINCT mp.id) AS media_projects_count,
                           COUNT(DISTINCT m.id) AS materials_count,
                           COUNT(DISTINCT obv.id) AS broadcasts_count,
                           COUNT(DISTINCT fm.id) AS foreign_materials_count
                       FROM\s
                           organization o
                           JOIN users u ON u.organization_id = o.id
                           JOIN employee e ON e.user_id = u.id
                           LEFT JOIN post p ON p.employee_id = e.id AND p.is_deleted = false
                           LEFT JOIN media_event me ON me.employee_id = e.id AND me.is_deleted = false
                           LEFT JOIN coverage c ON c.employee_id = e.id AND c.is_deleted = false
                           LEFT JOIN media_project mp ON mp.employee_id = e.id AND mp.is_deleted = false
                           LEFT JOIN material m ON m.employee_id = e.id AND m.is_deleted = false
                           LEFT JOIN online_broadcast_and_voice_chat_post obv ON obv.employee_id = e.id AND obv.is_deleted = false
                           LEFT JOIN foreign_materials fm ON fm.employee_id = e.id AND fm.is_deleted = false
                       WHERE
                           o.id = ?1
                       GROUP BY o.id;
            """, nativeQuery = true)
    List<OrganizationAllPostsCountProjection> getOrganizationAllPostsCount(UUID organizationId);

    @Query(value = """
            SELECT
                o.id AS id,
                o.name AS name,
                COALESCE(SUM(p.grade), 0) +
                COALESCE(SUM(me.grade), 0) +
                COALESCE(SUM(c.grade), 0) +
                COALESCE(SUM(mp.grade), 0) +
                COALESCE(SUM(m.grade), 0) +
                COALESCE(SUM(obv.grade), 0) +
                COALESCE(SUM(fm.grade), 0) AS total_grade
            FROM organization o
                     LEFT JOIN users u ON u.organization_id = o.id
                     LEFT JOIN employee e ON e.user_id = u.id
            
                     LEFT JOIN post p ON p.employee_id = e.id AND p.is_deleted = false
                     LEFT JOIN media_event me ON me.employee_id = e.id AND me.is_deleted = false
                     LEFT JOIN coverage c ON c.employee_id = e.id AND c.is_deleted = false
                     LEFT JOIN media_project mp ON mp.employee_id = e.id AND mp.is_deleted = false
                     LEFT JOIN material m ON m.employee_id = e.id AND m.is_deleted = false
                     LEFT JOIN online_broadcast_and_voice_chat_post obv ON obv.employee_id = e.id AND obv.is_deleted = false
                     LEFT JOIN foreign_materials fm ON fm.employee_id = e.id AND fm.is_deleted = false
            
            GROUP BY o.id, o.name
            ORDER BY total_grade DESC;
            """, nativeQuery = true)
    List<OrganizationsPostsProjection> getRatingUniversities();
}