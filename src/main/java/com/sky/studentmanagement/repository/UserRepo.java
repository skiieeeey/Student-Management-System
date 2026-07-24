package com.sky.studentmanagement.repository;

import com.sky.studentmanagement.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Long> {

    public boolean existsByUsername(String username);

    public Optional<Users> findByUsername(String username);

}
