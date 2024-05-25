package com.example.mppbackend.repository;
import com.example.mppbackend.api.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @EntityGraph(attributePaths = "roles")
    List<User> findAll();

    Optional<User> findByEmail(String email);
}

