package com.example.mppbackend.service;

import com.example.mppbackend.api.models.Role;
import com.example.mppbackend.api.models.User;
import com.example.mppbackend.repository.RoleRepository;
import com.example.mppbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserService() {}

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(String id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new Exception("The user doesn't exist!");
        }
        return user;
    }

    public boolean addUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean updateUser(String id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            if (!updatedUser.getPassword().isEmpty() && !passwordEncoder.matches(updatedUser.getPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    public Optional<User> getUserDetailsByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean checkUserLogin(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    public void addRoleToUser(String email, String roleName) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Role role = roleRepository.findByName(roleName);
            if (role == null) {
                role = new Role(roleName);
                roleRepository.save(role);
            }
            Set<Role> roles = user.getRoles();
            if (roles == null) {
                roles = new HashSet<>();
            }
            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);
        }
    }
}