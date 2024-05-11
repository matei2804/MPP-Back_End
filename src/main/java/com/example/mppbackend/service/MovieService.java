package com.example.mppbackend.service;
import com.example.mppbackend.api.models.Movie;
import com.example.mppbackend.api.models.User;
import com.example.mppbackend.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.github.javafaker.Faker;
import com.example.mppbackend.service.UserService;

import java.io.IOException;
import java.util.*;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private WebSocketService webSocketService;


    public MovieService(){

    }

    public Optional<Movie> getMovie(String id) throws Exception {
        Optional<Movie> movie = movieRepository.findById(id);
        if (!movie.isPresent()) {
            throw new Exception("The movie doesn't exist!");
        }
        return movie;
    }

    public List<Movie> getMovieList()
    {
        return movieRepository.findAll();
    }

    public boolean addMovie(Movie movie) {
        if (!movieRepository.existsById(movie.getId())) {
            movieRepository.save(movie);
            return true;
        }
        return false;
    }

    public boolean deleteMovie(String id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean updateMovie(String id, Movie updatedMovie){
        return movieRepository.findById(id).map(movie -> {
            movie.setTitle(updatedMovie.getTitle());
            movie.setGenre(updatedMovie.getGenre());
            movie.setTrailerLink(updatedMovie.getTrailerLink());
            movie.setPhoto(updatedMovie.getPhoto());
            movie.setYearOfRelease(updatedMovie.getYearOfRelease());
            movie.setUserID(updatedMovie.getUserID());
            movieRepository.save(movie);
            return true;
        }).orElse(false);
    }

    public List<Movie> getMoviesByUserID(String userId)
    {
        return movieRepository.findByUserId(userId);
    }
}
