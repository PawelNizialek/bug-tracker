package com.example.ztpai.service;

import com.example.ztpai.dto.UserRequest;
import com.example.ztpai.dto.UserResponse;
import com.example.ztpai.model.Role;
import com.example.ztpai.model.User;
import com.example.ztpai.repository.RoleRepository;
import com.example.ztpai.repository.UserRepository;
import com.example.ztpai.security.PasswordConfig;
import com.example.ztpai.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordConfig passwordConfig;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordConfig passwordConfig) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordConfig = passwordConfig;
    }

    private List<UserResponse> mapUserToUserResponse(List<User> users){
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user: users) {
            UserResponse userResponse = UserResponse.builder()
                    .userId(user.getUserId())
                    .created_at(user.getCreated_at())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .enabled(user.getEnabled())
                    .role(user.getRoles().iterator().next().getRoleName().name())
                    .build();
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        return mapUserToUserResponse(users);
    }

    public UserResponse getUser(Long userId){
        Optional<User> user = userRepository.findById(userId);
        return user.map(value -> UserResponse.builder()
                .created_at(value.getCreated_at())
                .email(value.getEmail())
                .firstName(value.getFirstName())
                .lastName(value.getLastName())
                .enabled(value.getEnabled())
                .build()).orElse(null);
    }

    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }

    public void editUser(UserRequest userRequest){
        User user = userRepository.findById(userRequest.getUserId()).orElseThrow();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        if(!userRequest.getPassword().equals(""))
            user.setPassword(passwordConfig.passwordEncoder().encode(userRequest.getPassword()));
        userRepository.save(user);
    }

    public UserResponse enableUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setEnabled(true);
        userRepository.save(user);
        return new UserResponse(user.getUserId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getEnabled(), user.getCreated_at(),
                user.getRoles().iterator().next().getRoleName().name());
    }

    public UserResponse disableUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setEnabled(false);
        userRepository.save(user);
        return new UserResponse(user.getUserId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getEnabled(), user.getCreated_at(),
                user.getRoles().iterator().next().getRoleName().name());
    }

    public void requestAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Role role = roleRepository.findByRoleName(UserRole.ADMIN).orElseThrow();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }
}
