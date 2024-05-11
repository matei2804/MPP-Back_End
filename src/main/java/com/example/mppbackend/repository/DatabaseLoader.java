package com.example.mppbackend.repository;

import com.example.mppbackend.api.models.Movie;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.mppbackend.api.models.User;
import com.github.javafaker.Faker;
import java.util.UUID;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public DatabaseLoader(UserRepository userRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public User generateFakeUser() {
        Faker faker = new Faker();
        String id = UUID.randomUUID().toString();
        String email = faker.internet().emailAddress();
        String name = faker.name().fullName();
        return new User(id, email, name, UUID.randomUUID().toString());
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
        if (userRepository.count() == 0) {
            userRepository.save(new User(UUID.randomUUID().toString(), "alice@example.com", "Alice", "123"));
            userRepository.save(new User(UUID.randomUUID().toString(), "bob@example.com", "Bob", UUID.randomUUID().toString()));
            userRepository.save(new User(UUID.randomUUID().toString(), "charlie@example.com", "Charlie", UUID.randomUUID().toString()));
            userRepository.save(new User(UUID.randomUUID().toString(), "admin@admin.com", "admin", "admin"));
            for (int i = 0; i < 100; i++) {
                User user = generateFakeUser();
                userRepository.save(user);
            }
        }

        if(movieRepository.count() == 0)
        {
            Faker faker = new Faker();
            User user1 = userRepository.findAll().get(faker.random().nextInt(0, (int) userRepository.count() - 1));
            User user2 = userRepository.findAll().get(faker.random().nextInt(0, (int) userRepository.count() - 1));
            User user3 = userRepository.findAll().get(faker.random().nextInt(0, (int) userRepository.count() - 1));
            User user4 = userRepository.findAll().get(faker.random().nextInt(0, (int) userRepository.count() - 1));
            User user5 = userRepository.findAll().get(faker.random().nextInt(0, (int) userRepository.count() - 1));
            User user6 = userRepository.findAll().get(faker.random().nextInt(0, (int) userRepository.count() - 1));

            movieRepository.save(new Movie("1", "The Shawshank Redemption", "Drama", 1994, "https://www.youtube.com/watch?v=NmzuHjWmXOc&ab_channel=RottenTomatoesClassicTrailers", "https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_.jpg", user1.getId()));
            movieRepository.save(new Movie("2", "The Godfather", "Crime, Drama", 1972, "https://www.youtube.com/watch?v=sY1S34973zA", "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg", user2.getId()));
            movieRepository.save(new Movie("3", "Inception", "Action, Adventure, Sci-Fi", 2010, "https://www.youtube.com/watch?v=YoHD9XEInc0", "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_.jpg", user3.getId()));
            movieRepository.save(new Movie("4", "Forrest Gump", "Drama, Romance", 1994, "https://www.youtube.com/watch?v=bLvqoHBptjg", "https://m.media-amazon.com/images/M/MV5BNWIwODRlZTUtY2U3ZS00Yzg1LWJhNzYtMmZiYmEyNmU1NjMzXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_FMjpg_UX1000_.jpg", user4.getId()));
            movieRepository.save(new Movie("5", "The Matrix", "Action, Sci-Fi", 1999, "https://www.youtube.com/watch?v=m8e-FF8MsqU", "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_.jpg", user5.getId()));
            movieRepository.save(new Movie("6", "Pulp Fiction", "Crime, Drama", 1994, "https://www.youtube.com/watch?v=s7EdQ4FqbhY", "https://m.media-amazon.com/images/M/MV5BNGNhMDIzZTUtNTBlZi00MTRlLWFjM2ItYzViMjE3YzI5MjljXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg", user6.getId()));

            for(int i = 0; i < 10; i++)
            {
                movieRepository.save(generateFakeMovie());
            }
        }

        if (userRepository.count() == 12345) {
            Faker faker = new Faker();

            for (int i = 0; i < 100000; i++) {
                User user = generateFakeUser();
                userRepository.save(user);
                for(int j = 0; j < 10000; j++)
                {
                    String[] genres = {"Action", "Adventure", "Comedy", "Drama", "Crime", "Romance", "Sci-Fi", "Thriller"};
                    String genre = genres[faker.random().nextInt(genres.length)];
                    String id = UUID.randomUUID().toString();

                    Movie movie = new Movie(
                            id,
                            faker.book().title(),
                            genre,
                            faker.number().numberBetween(1850, 2024),
                            "https://www.youtube.com/watch?v=" + faker.regexify("[A-Za-z0-9_-]{11}"),
                            "https://www.imageplaceholder.com/" + faker.letterify("??????"),
                            user.getId()
                    );
                    movieRepository.save(movie);
                }
            }
        }
    }
}
