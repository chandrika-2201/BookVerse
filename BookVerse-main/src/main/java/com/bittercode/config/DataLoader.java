package com.bittercode.config;

import com.bittercode.model.User;
import com.bittercode.model.UserRole;
import com.bittercode.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create default Admin/Seller user if not exists
        if (userRepository.findByEmailIdAndPasswordAndRole("admin", "admin", UserRole.SELLER).isEmpty()) {
            User admin = new User();
            admin.setEmailId("admin");
            admin.setPassword("admin");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setAddress("System");
            admin.setPhone(1234567890L);
            admin.setMailId("admin@system.com");
            admin.setRole(UserRole.SELLER);
            
            userRepository.save(admin);
            System.out.println("Default Admin user created. Username: admin, Password: admin");
        }
    }
}
