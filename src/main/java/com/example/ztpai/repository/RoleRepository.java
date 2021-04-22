package com.example.ztpai.repository;

import com.example.ztpai.model.Role;
import com.example.ztpai.security.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(UserRole role);
}
