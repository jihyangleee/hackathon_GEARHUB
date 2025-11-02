package com.example.demo;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initData(UserRepository userRepository, ProductRepository productRepository, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        // Data seeding removed: rely on the provided MySQL dump for initial data.
        return args -> {
            // No-op
        };
    }
}
