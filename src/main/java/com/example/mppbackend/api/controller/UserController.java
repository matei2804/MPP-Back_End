package com.example.mppbackend.api.controller;

import com.example.mppbackend.api.models.User;
import com.example.mppbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestParam String id) {
        try {
            Optional<User> user = userService.getUser(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/userList")
    public ResponseEntity<?> getUserList(@RequestParam(required = false) boolean withRoles) {
        try {
            List<User> userList;
            if (withRoles) {
                userList = userService.getUserList();
            } else {
                userList = userService.getUserList();
            }
            return ResponseEntity.ok(userList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(value = "/user", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            boolean status = userService.addUser(user);
            if (status) {
                return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with the same id already exists.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the user.");
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User roles: " + authentication.getAuthorities());
        boolean status = userService.deleteUser(id);
        if (status) {
            return ResponseEntity.ok("User with id " + id + " was deleted successfully.");
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id: " + id + " was not found!");
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @Secured("ROLE_ADMIN")
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User user) {
        boolean status = userService.updateUser(id, user);
        if (status) {
            return ResponseEntity.ok("User with id " + id + " was updated successfully.");
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id: " + id + " was not found!");
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @Secured("ROLE_ADMIN")
    @PutMapping("/user/updateUserRole/{email}")
    public ResponseEntity<?> updateUserRole(@PathVariable String email, @RequestBody Map<String, String> request) {
        String role = request.get("role");
        boolean status = userService.addRoleToUser(email, role);

        if (status) {
            return ResponseEntity.ok(Map.of("email", email, "role", role));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User with email " + email + " was not found!"));
        }
    }

}
