package com.sky.studentmanagement.repository;

import com.sky.studentmanagement.dto.projections.EnrollmentProjection;
import com.sky.studentmanagement.dto.projections.EnrollmentViewProjection;
import com.sky.studentmanagement.model.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
