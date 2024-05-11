package com.example.mppbackend;

import com.example.mppbackend.api.models.Movie;
import com.example.mppbackend.api.models.User;
import com.example.mppbackend.service.MovieService;
import com.example.mppbackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;


@WebMvcTest
@AutoConfigureMockMvc
public class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @MockBean
    private UserService userService;

    @Test
    public void testGetMovie() throws Exception {
        Movie movie = new Movie("1", "The Shawshank Redemption", "Drama", 1994, "https://www.youtube.com/watch?v=NmzuHjWmXOc&ab_channel=RottenTomatoesClassicTrailers", "https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_.jpg", "40d6c23b-da42-40da-b6fb-a79a7a223658");
        when(movieService.getMovie("1")).thenReturn(Optional.of(movie));

        mockMvc.perform(get("/movie?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("The Shawshank Redemption"));

        verify(movieService).getMovie("1");
    }

    @Test
    public void testGetNonExistingMovie() throws Exception {
        when(movieService.getMovie("999")).thenThrow(new Exception("The movie doesn't exist!"));

        mockMvc.perform(get("/movie?id=999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("The movie doesn't exist!"));

        verify(movieService).getMovie("999");
    }

    @Test
    public void testAddMovie() throws Exception {
        User newUser = new User("-1234", "haha@gmail.com", "haha", "123");
        userService.addUser(newUser);
        Movie newMovie = new Movie("11245", "New Movie", "New Genre", 2023, "https://newmovie.com/trailer", "https://newmovie.com/photo", "-1234");
        when(movieService.addMovie(any(Movie.class))).thenReturn(true);

        mockMvc.perform(post("/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newMovie)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Movie added successfully."));

        movieService.deleteMovie("11245");
        userService.deleteUser("-1234");
    }

    @Test
    public void testDeleteMovie() throws Exception{
        User newUser = new User("-1234", "haha@gmail.com", "haha", "123");
        userService.addUser(newUser);
        Movie newMovie = new Movie("11245", "New Movie", "New Genre", 2023, "https://newmovie.com/trailer", "https://newmovie.com/photo", "-1234");
        movieService.addMovie(newMovie);

        when(movieService.deleteMovie(any(String.class))).thenReturn(true);

        mockMvc.perform(delete("/movie/11245"))
                .andExpect(status().isOk())
                .andExpect(content().string("Movie with id 11245 was deleted successfully."));
        userService.deleteUser("-1234");
    }

    @Test
    public void testDeleteMoviefail() throws Exception{
        when(movieService.deleteMovie(any(String.class))).thenReturn(false);

        mockMvc.perform(delete("/movie/NU_EXISTA_ASDASD"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Movie with id: NU_EXISTA_ASDASD was not found!"));

    }

    @Test
    public void updateDeleteMovie() throws Exception{
        User newUser = new User("-1234", "haha@gmail.com", "haha", "123");
        userService.addUser(newUser);
        Movie newMovie = new Movie("11245", "New Movie", "New Genre", 2023, "https://newmovie.com/trailer", "https://newmovie.com/photo", "-1234");
        movieService.addMovie(newMovie);

         when(movieService.updateMovie(any(String.class), any(Movie.class))).thenReturn(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String movieJson = objectMapper.writeValueAsString(newMovie);


        mockMvc.perform(put("/movie/{id}", "11245")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Movie with id 11245 was updated successfully."));

        movieService.deleteMovie("11245");
        userService.deleteUser("-1234");
    }

    @Test
    public void testGetMovieList() throws Exception{
        mockMvc.perform(get("/movieList"))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetUser() throws Exception {
        User user = new User("u123", "user@example.com", "John Doe", "123");
        when(userService.getUser("u123")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user?id=u123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("u123"))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testGetNonExistingUser() throws Exception {
        when(userService.getUser("u999")).thenThrow(new Exception("User not found"));

        mockMvc.perform(get("/user?id=u999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    public void testAddUser() throws Exception {
        User newUser = new User("u124", "newuser@example.com", "New User", "123");
        when(userService.addUser(any(User.class))).thenReturn(true);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User added successfully."));
    }

    @Test
    public void testAddExistingUser() throws Exception {
        User existingUser = new User("u125", "existinguser@example.com", "Existing User", "123");
        when(userService.addUser(any(User.class))).thenReturn(false);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(existingUser)))
                .andExpect(status().isConflict())
                .andExpect(content().string("A user with the same id already exists."));
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(userService.deleteUser("u126")).thenReturn(true);

        mockMvc.perform(delete("/user/u126"))
                .andExpect(status().isOk())
                .andExpect(content().string("User with id u126 was deleted successfully."));
    }

    @Test
    public void testDeleteNonExistingUser() throws Exception {
        when(userService.deleteUser("u127")).thenReturn(false);

        mockMvc.perform(delete("/user/u127"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with id: u127 was not found!"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User updatedUser = new User("u128", "update@example.com", "Updated User", "123");
        when(userService.updateUser(eq("u128"), any(User.class))).thenReturn(true);

        mockMvc.perform(put("/user/{id}", "u128")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("User with id u128 was updated successfully."));
    }

    @Test
    public void testUpdateNonExistingUser() throws Exception {
        User nonExistingUser = new User("u129", "nonexisting@example.com", "Non Existing User", "123");
        when(userService.updateUser(eq("u129"), any(User.class))).thenReturn(false);

        mockMvc.perform(put("/user/{id}", "u129")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(nonExistingUser)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with id: u129 was not found!"));
    }



}
