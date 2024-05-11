package com.example.mppbackend.api.controller;

import com.example.mppbackend.api.models.Movie;
import com.example.mppbackend.api.models.User;
import com.example.mppbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<?> getUserList(){
        try{
            List<User> userList = userService.getUserList();
            return ResponseEntity.ok(userList);
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/user")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        boolean status = userService.addUser(user);
        if(status)
        {
            return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully.");
        }
        else return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with the same id already exists.");
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        boolean status = userService.deleteUser(id);
        if(status)
        {
            return ResponseEntity.ok("User with id " + id + " was deleted successfully.");
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id: " + id + " was not found!");
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User user) {
        boolean status = userService.updateUser(id, user);
        if(status)
        {
            return ResponseEntity.ok("User with id " + id + " was updated successfully.");
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id: " + id + " was not found!");
    }
}
