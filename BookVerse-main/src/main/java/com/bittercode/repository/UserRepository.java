package com.bittercode.repository;

import com.bittercode.model.User;
import com.bittercode.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailIdAndPasswordAndRole(String emailId, String password, UserRole role);
}
