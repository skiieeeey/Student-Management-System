package com.sky.studentmanagement.repository;

import com.sky.studentmanagement.dto.DashboardListDto;
import com.sky.studentmanagement.dto.projections.EnrollmentProjection;
import com.sky.studentmanagement.dto.projections.EnrollmentViewProjection;
import com.sky.studentmanagement.model.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EnrollmentRepo extends JpaRepository<Enrollment, Long> {

    @Query(value = "SELECT s.id AS id, s.first_name AS firstName, s.last_name AS lastName, s.email AS email, " +
            "COUNT(c.id) AS coursesCount, COALESCE(SUM(c.fee), 0) AS totalFee " +
            "FROM students s " +
            "JOIN enrollment e ON s.id = e.student_id " +
            "JOIN courses c ON e.course_id = c.id " +
            "WHERE s.active = true AND c.active = true " +
            "GROUP BY s.id, s.first_name, s.last_name, s.email",
            countQuery = "SELECT COUNT(DISTINCT s.id) FROM students s " +
                    "JOIN enrollment e ON s.id = e.student_id " +
                    "JOIN courses c ON e.course_id = c.id " +
                    "WHERE s.active = true AND c.active = true ",
            nativeQuery = true)
    Page<EnrollmentProjection> fetchActiveStudentSummaries(Pageable pageable);


    @Query(value =  "SELECT s.id AS id, s.first_name AS firstName, s.last_name AS lastName, s.email AS email, \n" +
                    "COUNT(c.id) AS coursesCount, COALESCE(SUM(c.fee), 0) AS totalFee, \n" +
                    "JSONB_AGG( \n" +
                        "JSONB_BUILD_OBJECT( \n" +
                            "'courseName', c.course_name, \n" +
                            "'description', COALESCE(NULLIF(c.description, ''), 'No Description'), \n" +
                            "'fee', c.fee \n" +
                        ") \n" +
                    ") AS courseJsonList \n" +
                    "FROM students s \n" +
                    "JOIN enrollment e ON s.id = e.student_id \n" +
                    "JOIN courses c ON e.course_id = c.id \n" +
                    "WHERE s.active = true and c.active = true and s.id = :student_id \n" +
                    "GROUP BY s.id, s.first_name, s.last_name, s.email",
            nativeQuery = true)
    EnrollmentViewProjection findByStudentSummaryById(@Param("student_id") Long id);

    List<Enrollment> findByStudentId(Long studentId);

    @Query( "SELECT c.courseCode, COUNT(e) " +
            "FROM Enrollment e " +
            "JOIN e.course c " +
            "GROUP BY c.courseCode " +
            "ORDER BY COUNT(e) DESC")
    List<Object[]> findTopCourse(Pageable pageable);


    @Query("SELECT COUNT(e) " +
            "FROM Enrollment e " +
            "WHERE e.enrolledAt BETWEEN :startOfDay AND :endOfDay")
    long countTodayEnrollments(@Param("startOfDay") LocalDateTime startOfDay,
                               @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT COUNT(e) " +
            "FROM Enrollment e " +
            "WHERE e.enrolledAt BETWEEN :startOfMonth AND :endOfMonth")
    long countMonthEnrollments(@Param("startOfMonth") LocalDateTime startOfMonth,
                               @Param("endOfMonth") LocalDateTime endOfMonth);


    @Query("SELECT new com.sky.studentmanagement.dto.DashboardListDto(" +
            "s.id, " +
            "CONCAT(s.firstName, ' ', s.lastName), " +
            "CONCAT(UPPER(SUBSTRING(s.firstName, 1, 1)), UPPER(SUBSTRING(s.lastName, 1, 1))), " +
            "c.courseName, " +
            "CAST(DATE(e.enrolledAt) AS java.time.LocalDate), " +
            "c.duration) " +
            "FROM Enrollment e " +
            "JOIN e.student s " +
            "JOIN e.course c " +
            "ORDER BY e.enrolledAt DESC")
    Page<DashboardListDto> getAllRecentEnrollments(Pageable pageable);
}
