package com.example.mppbackend.api.controller;
import com.example.mppbackend.api.models.Movie;
import com.example.mppbackend.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService)
    {
        this.movieService = movieService;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/movie")
    public ResponseEntity<?> getMovie(@RequestParam String id) {
        try {
            Optional<Movie> movie = movieService.getMovie(id);
            return ResponseEntity.ok(movie);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/movieList")
    public ResponseEntity<?> getMovieList(){
       try{
           List<Movie> movieList = movieService.getMovieList();
           return ResponseEntity.ok(movieList);
       }catch (Exception e)
       {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
       }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/movie")
    public ResponseEntity<?> addMovie(@RequestBody Movie movie) {
        boolean status = movieService.addMovie(movie);
        if(status)
        {
            return ResponseEntity.status(HttpStatus.CREATED).body("Movie added successfully.");
        }
        else return ResponseEntity.status(HttpStatus.CONFLICT).body("A movie with the same id already exists.");
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/movie/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable String id) {
        boolean status = movieService.deleteMovie(id);
        if(status)
        {
            return ResponseEntity.ok("Movie with id " + id + " was deleted successfully.");
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie with id: " + id + " was not found!");
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/movie/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable String id, @RequestBody Movie movie) {
        boolean status = movieService.updateMovie(id, movie);
        if(status)
        {
            return ResponseEntity.ok("Movie with id " + id + " was updated successfully.");
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie with id: " + id + " was not found!");
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/movies/user/{id}")
    public ResponseEntity<?> getMoviesByUserId(@PathVariable String id) {
        try {
            List<Movie> movies = movieService.getMoviesByUserID(id);
            if (movies.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No movies found for user ID: " + id);
            }
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
