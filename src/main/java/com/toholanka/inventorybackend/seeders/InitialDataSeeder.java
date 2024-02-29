package com.toholanka.inventorybackend.seeders;

import com.toholanka.inventorybackend.model.AuthenticationToken;
import com.toholanka.inventorybackend.model.Users;
import com.toholanka.inventorybackend.repository.TokenRepository;
import com.toholanka.inventorybackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class InitialDataSeeder implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(InitialDataSeeder.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Optional<Users> existingUser = userRepository.findByEmail("admin@toholanka.com");
        if (existingUser.isEmpty()) {
            Users adminUser = createAdminUser();
            createAdminToken(adminUser);
        }
    }

    private Users createAdminUser() {
        Users adminUser = new Users();
        adminUser.setEmail("admin@toholanka.com");
        adminUser.setPassword("toholanka");
        adminUser.setFirstName("TOHO");
        adminUser.setLastName("LANKA");
        adminUser.setRole("ADMIN");
        adminUser.setUserName("TOHO LANKA");
        adminUser.setVerified(true);

        Users savedUser = userRepository.save(adminUser);
        log.info("Admin user created successfully");
        return savedUser;
    }

    private void createAdminToken(Users user) {
        AuthenticationToken token = new AuthenticationToken(user);
        tokenRepository.save(token);
    }
}
