package uz.otfiv.universitymediamonitoringsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.otfiv.universitymediamonitoringsystem.entity.Post;
import uz.otfiv.universitymediamonitoringsystem.projection.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query(value = "select p.* from post p inner join showed_media s on p.showed_media_id = s.id where s.post_type = ?1", nativeQuery = true)
    List<Post> findAllByPostType(String postType);

    Optional<Post> findPostByIdAndIsDeletedFalse(UUID postId);

    List<Post> findAllByCreatedAtAfterAndCreatedAtBefore(LocalDateTime createdAt, LocalDateTime createdAt2);

    List<Post> findByEmployeeIdAndIsDeletedFalse(UUID id);

    List<Post> findPostsByEmployeeIdAndIsDeletedFalse(UUID id);

    List<Post> findPostsByEmployeeIdAndIsDeletedTrue(UUID id);

    Optional<Post> findPostByIdAndIsDeletedTrue(UUID postId);

    List<Post> findPostsByAdminIdAndIsDeletedFalse(UUID id);

    List<Post> findPostsByAdminIdAndIsDeletedTrue(UUID id);

    @Query(value = """
            WITH EmployeePostStats AS (
                SELECT e.id AS employee_id, p.post_type, COUNT(p.id) AS post_count, SUM(p.grade) AS total_grade, o.name AS organization_name
                FROM post p
                         INNER JOIN employee e ON p.employee_id = e.id
                         INNER JOIN users u ON e.user_id = u.id
                         INNER JOIN organization o ON o.id = u.organization_id
                WHERE p.is_deleted = false
                GROUP BY e.id, p.post_type, o.name
            ),
            MaxPostCount AS (
                SELECT post_type, MAX(post_count) AS max_post_count
                FROM EmployeePostStats
                GROUP BY post_type
            ),
            SelectedEmployees AS (
                SELECT eps.post_type, eps.post_count, eps.total_grade, eps.organization_name,
                       ROW_NUMBER() OVER (PARTITION BY eps.post_type ORDER BY eps.post_count DESC, eps.total_grade DESC) AS rn
                FROM EmployeePostStats eps
                     INNER JOIN MaxPostCount mpc ON eps.post_type = mpc.post_type AND eps.post_count = mpc.max_post_count
            )
            SELECT post_type, post_count, total_grade, organization_name
            FROM SelectedEmployees
            WHERE rn = 1
            ORDER BY post_type, post_count DESC, total_grade DESC;
            """, nativeQuery = true)
    List<TopFiveStatisticProjection> getTopFive();


    @Query(value = """
            SELECT 'Posts' AS postType, COUNT(*) AS count FROM post WHERE is_deleted = false
                  UNION ALL
                  SELECT 'Media Events' AS postType, COUNT(*) AS count FROM media_event WHERE is_deleted = false
                  UNION ALL
                  SELECT 'Coverages' AS postType, COUNT(*) AS count FROM coverage WHERE is_deleted = false
                  UNION ALL
                  SELECT 'Media Projects' AS postType, COUNT(*) AS count FROM media_project WHERE is_deleted = false
                  UNION ALL
                  SELECT 'Materials' AS postType, COUNT(*) AS count FROM material WHERE is_deleted = false
                  UNION ALL
                  SELECT 'Broadcasts' AS postType, COUNT(*) AS count FROM online_broadcast_and_voice_chat_post WHERE is_deleted = false
                  UNION ALL
                  SELECT 'Foreign Materials' AS postType, COUNT(*) AS count FROM foreign_materials WHERE is_deleted = false
            """, nativeQuery = true)
    List<Post> findAllByIsDeletedFalse();

    @Query(value = """
            WITH EmployeePostStats AS (
                SELECT e.id AS employee_id,
                       p.post_type,
                       COUNT(p.id) AS post_count,
                       SUM(p.grade) AS total_grade,
                       o.name AS organization_name,
                       CASE
                           WHEN EXTRACT(MONTH FROM p.created_at) BETWEEN 1 AND 3 THEN 'Q1'
                           WHEN EXTRACT(MONTH FROM p.created_at) BETWEEN 4 AND 6 THEN 'Q2'
                           WHEN EXTRACT(MONTH FROM p.created_at) BETWEEN 7 AND 9 THEN 'Q3'
                           WHEN EXTRACT(MONTH FROM p.created_at) BETWEEN 10 AND 12 THEN 'Q4'
                       END AS quarter
                FROM post p
                         INNER JOIN employee e ON p.employee_id = e.id
                         INNER JOIN users u ON e.user_id = u.id
                         INNER JOIN organization o ON o.id = u.organization_id
                WHERE p.is_deleted = false
                  AND EXTRACT(YEAR FROM p.created_at) = EXTRACT(YEAR FROM CURRENT_DATE)
                GROUP BY e.id, p.post_type, o.name, quarter
            ),
                 RankedPosts AS (
                     SELECT employee_id, post_type, post_count, total_grade, organization_name, quarter,
                            ROW_NUMBER() OVER (PARTITION BY post_type, quarter ORDER BY post_count DESC, total_grade DESC) AS rn
                     FROM EmployeePostStats
                 )
            SELECT employee_id, post_type, post_count, total_grade, organization_name, quarter
            FROM RankedPosts
            WHERE rn = 1
            ORDER BY post_type, quarter, post_count DESC, total_grade DESC;
            """, nativeQuery = true)
    List<TopFiveQuoter> getTopFiveQuoter();

    @Query(value = """
            SELECT
                o.id AS id,
                o.name AS name,
                COALESCE((SELECT COUNT(p.id)
                          FROM post p
                                   JOIN employee e ON p.employee_id = e.id
                                   JOIN users u ON e.user_id = u.id
                          WHERE u.organization_id = o.id AND p.is_deleted = false
                            AND p.created_at >= date_trunc('month', CURRENT_DATE) - INTERVAL '1 month'), 0) +
                COALESCE((SELECT COUNT(me.id)
                          FROM media_event me
                                   JOIN employee e ON me.employee_id = e.id
                                   JOIN users u ON e.user_id = u.id
                          WHERE u.organization_id = o.id AND me.is_deleted = false
                            AND me.created_at >= date_trunc('month', CURRENT_DATE) - INTERVAL '1 month'), 0) +
                COALESCE((SELECT COUNT(c.id)
                          FROM coverage c
                                   JOIN employee e ON c.employee_id = e.id
                                   JOIN users u ON e.user_id = u.id
                          WHERE u.organization_id = o.id AND c.is_deleted = false
                            AND c.created_at >= date_trunc('month', CURRENT_DATE) - INTERVAL '1 month'), 0) +
                COALESCE((SELECT COUNT(mp.id)
                          FROM media_project mp
                                   JOIN employee e ON mp.employee_id = e.id
                                   JOIN users u ON e.user_id = u.id
                          WHERE u.organization_id = o.id AND mp.is_deleted = false
                            AND mp.created_at >= date_trunc('month', CURRENT_DATE) - INTERVAL '1 month'), 0) +
                COALESCE((SELECT COUNT(m.id)
                          FROM material m
                                   JOIN employee e ON m.employee_id = e.id
                                   JOIN users u ON e.user_id = u.id
                          WHERE u.organization_id = o.id AND m.is_deleted = false
                            AND m.created_at >= date_trunc('month', CURRENT_DATE) - INTERVAL '1 month'), 0) +
                COALESCE((SELECT COUNT(obv.id)
                          FROM online_broadcast_and_voice_chat_post obv
                                   JOIN employee e ON obv.employee_id = e.id
                                   JOIN users u ON e.user_id = u.id
                          WHERE u.organization_id = o.id AND obv.is_deleted = false
                            AND obv.created_at >= date_trunc('month', CURRENT_DATE) - INTERVAL '1 month'), 0) +
                COALESCE((SELECT COUNT(fm.id)
                          FROM foreign_materials fm
                                   JOIN employee e ON fm.employee_id = e.id
                                   JOIN users u ON e.user_id = u.id
                          WHERE u.organization_id = o.id AND fm.is_deleted = false
                            AND fm.created_at >= date_trunc('month', CURRENT_DATE) - INTERVAL '1 month'), 0) AS total_post_count
            FROM organization o
            ORDER BY total_post_count DESC limit 10;
            """, nativeQuery = true)
    List<TopLastMonthPostsProjection> getTopMonthPosts();

    @Query(value = """
            select p.employee_id,p.id, p.link, p.date_time, p.showed_user, p.media, p.show, p.stuff, p.scale, p.post_type, p.grade from post p where p.is_deleted = false and p.employee_id = ?1
            """, nativeQuery = true)
    List<GetAllPostsProjection> getAllPostsByEmployeeId(UUID id);

    @Query(value = """
            SELECT
                p.post_type AS postType,
                COUNT(p) AS postCount
            FROM Post p
            WHERE p.is_deleted = false and employee_id is not null
            GROUP BY p.post_type
            """, nativeQuery = true)
    List<PostTypeCountProjection> getPostCountByType();
}