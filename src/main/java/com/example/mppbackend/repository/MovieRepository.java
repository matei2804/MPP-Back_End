package com.example.mppbackend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.mppbackend.api.models.Movie;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String>{
    List<Movie> findByUserId(String userId);
}
