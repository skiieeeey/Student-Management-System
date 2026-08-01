package com.sky.studentmanagement.repository;

import com.sky.studentmanagement.model.Students;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<Students, Long> {

    boolean existsByEmailAndPhoneIgnoreCaseAndIdNot(String email, String phone, Long id);

    Page<Students> findByActiveTrue(Pageable pageable);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByPhoneIgnoreCaseAndIdNot(String phone, Long id);

    List<Students> findByActiveTrue(Sort sort);
}
