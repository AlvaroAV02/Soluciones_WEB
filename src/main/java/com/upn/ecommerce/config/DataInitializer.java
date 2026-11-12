package com.upn.ecommerce.config;

import com.upn.ecommerce.entity.Usuario;
import com.upn.ecommerce.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner crearUsuarios(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (!repo.existsByUsername("admin")) {
                repo.save(new Usuario("admin", encoder.encode("admin123"), "ADMIN"));
            }
            if (!repo.existsByUsername("user")) {
                repo.save(new Usuario("user", encoder.encode("user123"), "USER"));
            }
        };
    }
}
