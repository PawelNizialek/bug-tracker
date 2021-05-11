package com.example.ztpai.controller;

import com.example.ztpai.dto.RegistrationRequest;
import com.example.ztpai.dto.UserRequest;
import com.example.ztpai.dto.UserResponse;
import com.example.ztpai.service.AuthService;
import com.example.ztpai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @DeleteMapping("/delete/{userId}")
    public HttpStatus deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
        return HttpStatus.OK;
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editUser(@RequestBody UserRequest user){
        userService.editUser(user);
        return status(HttpStatus.OK).body(null);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody RegistrationRequest userRequest){
        return new ResponseEntity<>(authService.registerUser(userRequest), HttpStatus.CREATED);
    }

    @GetMapping("/enable/{userId}")
    public ResponseEntity<UserResponse> enableUser(@PathVariable("userId") Long userId){
        return status(HttpStatus.OK).body(userService.enableUser(userId));
    }

    @GetMapping("/disable/{userId}")
    public ResponseEntity<UserResponse> disableUser(@PathVariable("userId") Long userId){
        return status(HttpStatus.OK).body(userService.disableUser(userId));
    }

    @GetMapping("/request-admin/{userId}")
    public ResponseEntity<?> requestAdmin(@PathVariable("userId") Long userId){
        userService.requestAdmin(userId);
        return status(HttpStatus.OK).body(null);
    }

}
