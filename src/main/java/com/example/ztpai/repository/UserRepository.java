package com.example.ztpai.repository;

import com.example.ztpai.model.User;
import com.example.ztpai.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByFirstNameAndLastName(String firstName, String lastName);
    Optional<User> findByVerificationToken(VerificationToken token);
}
