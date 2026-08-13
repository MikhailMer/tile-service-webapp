package org.mikhail.tilewebsite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        String adminUsername = System.getenv("ADMIN_USERNAME");

        if (adminUsername == null || adminUsername.isEmpty()) {
            log.error("[SECURITY] Critical error: Environment variable 'ADMIN_USERNAME' is not set! Admin user creation aborted.");
            return;
        }

        if (userRepository.findByUsername(adminUsername).isEmpty()) {

            String rawPassword = System.getenv("ADMIN_PASSWORD");

            if (rawPassword == null || rawPassword.isEmpty()) {
                log.error("[SECURITY] Critical error: Environment variable 'ADMIN_PASSWORD' is not set! Admin user creation aborted.");
                return;
            }

            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setRole("ROLE_ADMIN");
            admin.setPassword(passwordEncoder.encode(rawPassword));

            userRepository.save(admin);
            log.info("[SECURITY] Administrator '{}' successfully created in the database.", adminUsername);
        } else {
            log.info("[SECURITY] Administrator '{}' already exists in the database. Skipping initialization.",  adminUsername);
        }
    }
}
