package com.example.mppbackend;

import com.example.mppbackend.api.models.Movie;
import com.example.mppbackend.api.models.User;
import com.example.mppbackend.service.MovieService;
import com.example.mppbackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ServiceTests {

    @Autowired
    private  MovieService movieService;

    @Autowired
    private UserService userService;

    public ServiceTests()
    {
    }

    @Test
    void testAddNewMovie() {
        User newUser = new User("-1234", "haha@gmail.com", "haha", "123");
        userService.addUser(newUser);
        Movie newMovie = new Movie("11245", "New Movie", "New Genre", 2023, "https://newmovie.com/trailer", "https://newmovie.com/photo", "-1234");
        assertThat(movieService.addMovie(newMovie)).isTrue();
        movieService.deleteMovie("11245");
        userService.deleteUser("-1234");
    }

    @Test
    void testAddExistingMovie() {
        Movie existingMovie = movieService.getMovieList().get(0);
        assertThat(movieService.addMovie(existingMovie)).isFalse();
    }

    @Test
    void testUpdateExistingMovie() throws Exception {
        User newUser = new User("-1234", "haha@gmail.com", "haha", "123");
        userService.addUser(newUser);
        Movie updateMovie = new Movie("11245", "New Movie", "New Genre", 2023, "https://newmovie.com/trailer", "https://newmovie.com/photo", "-1234");
        movieService.addMovie(updateMovie);

        assertThat(movieService.updateMovie("11245", updateMovie)).isTrue();
        Optional<Movie> result = movieService.getMovie("11245");
        assertThat(result.get().getTitle()).isEqualTo("New Movie");
        movieService.deleteMovie("11245");
        userService.deleteUser("-1234");
    }

    @Test
    void testUpdateNonExistingMovie() {
        User newUser = new User("-1234", "haha@gmail.com", "haha", "123");
        userService.addUser(newUser);

        Movie nonExistingMovie = new Movie("nu exista id", "Non-Existing Movie", "Drama", 2023, "https://nonexistingmovie.com/trailer", "https://nonexistingmovie.com/photo", "-1234");
        assertThat(movieService.updateMovie("nu exista id", nonExistingMovie)).isFalse();
        userService.deleteUser("-1234");
    }

    @Test
    void testDeleteNonExistingMovie() {
        assertThat(movieService.deleteMovie("999")).isFalse();
    }

    @Test
    void testGetExistingMovie() throws Exception {
        Optional<Movie> m = movieService.getMovie("1");
        Movie movie = m.get();

        assertThat(movie).isNotNull();
        assertThat(movie.getId()).isEqualTo("1");

        assertThat(movie.getId()).isEqualTo("1");
        assertThat(movie.getGenre()).isEqualTo("Drama");
        assertThat(movie.getTitle()).isEqualTo("The Shawshank Redemption");
        assertThat(movie.getYearOfRelease()).isEqualTo(1994);
        assertThat(movie.getTrailerLink()).isEqualTo("https://www.youtube.com/watch?v=NmzuHjWmXOc&ab_channel=RottenTomatoesClassicTrailers");
        assertThat(movie.getPhoto()).isEqualTo("https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_.jpg");

        movie.setId("-1");
        movie.setGenre("-1");
        movie.setYearOfRelease(-1);
        movie.setTitle("-1");
        movie.setPhoto("-1");
        movie.setTrailerLink("-1");

        assertThat(movie.getId()).isEqualTo("-1");
        assertThat(movie.getGenre()).isEqualTo("-1");
        assertThat(movie.getTitle()).isEqualTo("-1");
        assertThat(movie.getYearOfRelease()).isEqualTo(-1);
        assertThat(movie.getTrailerLink()).isEqualTo("-1");
        assertThat(movie.getPhoto()).isEqualTo("-1");

    }

    @Test
    void testAddNewUser() throws Exception {
        User newUser = new User("u123", "user@example.com", "John Doe", "123");
        assertThat(userService.addUser(newUser)).isTrue();
        Optional<User> fetchedUser = userService.getUser("u123");
        assertThat(fetchedUser.isPresent()).isTrue();
        assertThat(fetchedUser.get().getEmail()).isEqualTo("user@example.com");
        assertThat(userService.deleteUser("u123")).isTrue();
    }

    @Test
    void testAddExistingUser() {
        User existingUser = userService.getUserList().get(0);
        assertThat(userService.addUser(existingUser)).isFalse();
    }

    @Test
    void testDeleteExistingUser() throws Exception {
        User newUser = new User("u124", "delete@example.com", "Jane Doe", "123");
        userService.addUser(newUser);
        assertThat(userService.deleteUser("u124")).isTrue();
        assertThatThrownBy(() -> userService.getUser("u124"))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("The user doesn't exist!");
    }

    @Test
    void testUpdateExistingUser() throws Exception {
        User newUser = new User("u125", "update@example.com", "Jim Beam", "123");
        userService.addUser(newUser);
        newUser.setEmail("newemail@example.com");
        newUser.setName("Jimmy Beam");
        assertThat(userService.updateUser("u125", newUser)).isTrue();

        Optional<User> updatedUser = userService.getUser("u125");
        assertThat(updatedUser.isPresent()).isTrue();
        assertThat(updatedUser.get().getEmail()).isEqualTo("newemail@example.com");
        assertThat(updatedUser.get().getName()).isEqualTo("Jimmy Beam");
        assertThat(userService.deleteUser("u125")).isTrue();
    }

}
