package skopanko.projectmanagerapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import skopanko.projectmanagerapi.entity.AppUser;
import skopanko.projectmanagerapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminUsername = "szymon";
        String adminPassword = "szymon";

        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            AppUser admin = new AppUser();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setEmail("admin@example.com");
            userRepository.save(admin);
            logger.info("Admin user created with username: {} and password: {}", adminUsername, adminPassword);
        } else {
            logger.info("Admin user already exists with username: {}", adminUsername);
        }
    }
}
