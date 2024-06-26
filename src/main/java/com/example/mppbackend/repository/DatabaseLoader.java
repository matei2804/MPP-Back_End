package com.example.mppbackend.repository;

import com.example.mppbackend.api.models.Movie;
import com.example.mppbackend.api.models.Role;
import com.example.mppbackend.api.models.User;
import com.example.mppbackend.service.UserService;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    private final UserService userService;
    private final MovieRepository movieRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DatabaseLoader(UserRepository userRepository, UserService userService, MovieRepository movieRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.movieRepository = movieRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User generateFakeUser() {
        Faker faker = new Faker();
        String id = UUID.randomUUID().toString();
        String email = faker.internet().emailAddress();
        String name = faker.name().fullName();
        return new User(id, email, name, passwordEncoder.encode(UUID.randomUUID().toString()));
    }

    public Movie generateFakeMovie() {
        Faker faker = new Faker();

        String[] genres = {"Action", "Adventure", "Comedy", "Drama", "Crime", "Romance", "Sci-Fi", "Thriller"};
        String genre = genres[faker.random().nextInt(genres.length)];
        String id = UUID.randomUUID().toString();
        User user = userRepository.findAll().get(faker.random().nextInt(0, (int) userRepository.count() - 1));

        return new Movie(
                id,
                faker.book().title(),
                genre,
                faker.number().numberBetween(1850, 2024),
                "https://www.youtube.com/watch?v=" + faker.regexify("[A-Za-z0-9_-]{11}"),
                "https://www.imageplaceholder.com/" + faker.letterify("??????"),
                user.getId()
        );
    }

    @Override
    public void run(String... args) {
//        movieRepository.deleteAll();
//        userRepository.deleteAll();
//        roleRepository.deleteAll();

        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("ROLE_USER"));
            roleRepository.save(new Role("ROLE_MANAGER"));
            roleRepository.save(new Role("ROLE_ADMIN"));
        }

        if (userRepository.count() == 0) {
            Role userRole = roleRepository.findByName("ROLE_USER");
            Role managerRole = roleRepository.findByName("ROLE_MANAGER");
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");

            User admin = new User("1", "admin@admin.com", "admin", passwordEncoder.encode("admin"));
            admin.getRoles().add(adminRole);
            userRepository.save(admin);

            User manager = new User("2", "manager@manager.com", "manager", passwordEncoder.encode("manager"));
            manager.getRoles().add(managerRole);
            userRepository.save(manager);

            User user = new User("3", "user@user.com", "user", passwordEncoder.encode("user"));
            user.getRoles().add(userRole);
            userRepository.save(user);

            for (int i = 0; i < 30; i++) {
                User fakeUser = generateFakeUser();
                userService.addUser(fakeUser);
            }
        }

        if (movieRepository.count() == 0) {

            String user_id = "3";
            String manager_id = "2";
            String admin_id = "1";

            for (int i = 0; i < 5; i++) {
                Movie movie = generateFakeMovie();
                movie.setUserID(user_id);
                movieRepository.save(movie);
            }
            for (int i = 0; i < 5; i++) {
                Movie movie = generateFakeMovie();
                movie.setUserID(manager_id);
                movieRepository.save(movie);
            }
            for (int i = 0; i < 5; i++) {
                Movie movie = generateFakeMovie();
                movie.setUserID(admin_id);
                movieRepository.save(movie);
            }
        }
    }
}
