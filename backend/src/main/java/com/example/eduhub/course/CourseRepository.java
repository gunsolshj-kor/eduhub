package com.example.eduhub.course;

import com.example.eduhub.course.dto.CourseSummaryResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * 제목(부분 일치, 대소문자 무시)·분류로 검색하고 회차 수를 함께 센다. 조건이 null이면 거르지 않는다.
     * PostgreSQL은 null 파라미터의 타입을 bytea로 추정해 lower()가 실패하므로 cast(... as String)으로 타입을 정한다.
     * (H2에서는 이 문제가 드러나지 않는다)
     */
    @Query(
            value =
                    """
                    select new com.example.eduhub.course.dto.CourseSummaryResponse(
                        c.id, c.title, c.category, c.status, count(s))
                    from Course c left join c.sessions s
                    where (cast(:keyword as String) is null
                           or lower(c.title) like lower(concat('%', cast(:keyword as String), '%')))
                      and (cast(:category as String) is null or c.category = cast(:category as String))
                    group by c.id, c.title, c.category, c.status
                    """,
            countQuery =
                    """
                    select count(c) from Course c
                    where (cast(:keyword as String) is null
                           or lower(c.title) like lower(concat('%', cast(:keyword as String), '%')))
                      and (cast(:category as String) is null or c.category = cast(:category as String))
                    """)
    Page<CourseSummaryResponse> search(
            @Param("keyword") String keyword, @Param("category") String category, Pageable pageable);

    /** 상세 화면용. 회차를 한 번의 쿼리로 함께 가져온다. */
    @EntityGraph(attributePaths = "sessions")
    Optional<Course> findWithSessionsById(Long id);
}
