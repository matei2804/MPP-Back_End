package com.example.mppbackend.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "Users")
public class User {

    @Id
    private String id;
    @Column(name = "Email", nullable = false, unique = true)
    @Email(message = "Invalid email format")
    private String email;
    @Column(name = "Name", nullable = false)
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    @Column(name = "Password", nullable = false)
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    public User(String id, String email, String name, String password)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {

    }

    @PrePersist
    @PreUpdate
    private void hashPassword() {
        if (this.password != null && this.password.length() < 60) {
            this.password = new BCryptPasswordEncoder().encode(this.password);
        }
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
