package com.sky.studentmanagement.config;

import com.sky.studentmanagement.model.Users;
import com.sky.studentmanagement.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadSampleData(UserRepo userRepo, PasswordEncoder passwordEncoder){

        return args -> {
            if(!userRepo.existsByUsername("admin")) {
                Users users = new Users();
                users.setUsername("admin");
                users.setPassword(passwordEncoder.encode("admin@123"));
                users.setActive(true);
                userRepo.save(users);
            }
        };
    }

}
