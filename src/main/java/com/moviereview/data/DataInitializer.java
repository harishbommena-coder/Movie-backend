package com.moviereview.data;

import com.moviereview.entity.Movie;
import com.moviereview.entity.Review;
import com.moviereview.entity.ReviewStatus;
import com.moviereview.entity.Role;
import com.moviereview.entity.User;
import com.moviereview.repository.MovieRepository;
import com.moviereview.repository.ReviewRepository;
import com.moviereview.repository.UserRepository;
import com.moviereview.service.MovieService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final MovieService movieService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           MovieRepository movieRepository,
                           ReviewRepository reviewRepository,
                           MovieService movieService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
        this.movieService = movieService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            seedUsers();
        }
        if (movieRepository.count() == 0) {
            seedMoviesAndReviews();
        }
    }

    private void seedUsers() {
        // Admin account
        User admin = new User(
                "admin",
                "admin@movies.com",
                passwordEncoder.encode("admin123"),
                Role.ROLE_ADMIN
        );
        userRepository.save(admin);

        // Standard user accounts
        User user1 = new User(
                "john_doe",
                "john@movies.com",
                passwordEncoder.encode("user123"),
                Role.ROLE_USER
        );
        userRepository.save(user1);

        User user2 = new User(
                "sarah_connor",
                "sarah@movies.com",
                passwordEncoder.encode("user123"),
                Role.ROLE_USER
        );
        userRepository.save(user2);
    }

    private void seedMoviesAndReviews() {
        User admin = userRepository.findByUsername("admin").orElse(null);
        User john = userRepository.findByUsername("john_doe").orElse(null);
        User sarah = userRepository.findByUsername("sarah_connor").orElse(null);

        // Movie 1: Inception
        Movie inception = new Movie(
                "Inception",
                "Sci-Fi",
                2010,
                "Christopher Nolan",
                "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page, Tom Hardy",
                148,
                "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster.",
                "https://images.unsplash.com/photo-1534447677768-be436bb09401?auto=format&fit=crop&w=600&q=80",
                "https://www.youtube.com/embed/YoHD9XEInc0"
        );
        movieRepository.save(inception);

        // Movie 2: The Dark Knight
        Movie darkKnight = new Movie(
                "The Dark Knight",
                "Action",
                2008,
                "Christopher Nolan",
                "Christian Bale, Heath Ledger, Aaron Eckhart, Michael Caine",
                152,
                "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
                "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=600&q=80",
                "https://www.youtube.com/embed/EXeTwQWrcwY"
        );
        movieRepository.save(darkKnight);

        // Movie 3: Interstellar
        Movie interstellar = new Movie(
                "Interstellar",
                "Sci-Fi",
                2014,
                "Christopher Nolan",
                "Matthew McConaughey, Anne Hathaway, Jessica Chastain, Michael Caine",
                169,
                "When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot, Joseph Cooper, is tasked to pilot a spacecraft, along with a team of researchers, to find a new planet for humans.",
                "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=600&q=80",
                "https://www.youtube.com/embed/zSWdZVtXT7E"
        );
        movieRepository.save(interstellar);

        // Movie 4: Pulp Fiction
        Movie pulpFiction = new Movie(
                "Pulp Fiction",
                "Crime",
                1994,
                "Quentin Tarantino",
                "John Travolta, Uma Thurman, Samuel L. Jackson, Bruce Willis",
                154,
                "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption in Los Angeles.",
                "https://images.unsplash.com/photo-1594909122845-11baa439b7bf?auto=format&fit=crop&w=600&q=80",
                "https://www.youtube.com/embed/s7EdQ4FqbhY"
        );
        movieRepository.save(pulpFiction);

        // Movie 5: Spirited Away
        Movie spiritedAway = new Movie(
                "Spirited Away",
                "Animation",
                2001,
                "Hayao Miyazaki",
                "Rumi Hiiragi, Miyu Irino, Mari Natsuki, Takashi Naito",
                125,
                "During her family's move to the suburbs, a sullen 10-year-old girl wanders into a world ruled by gods, witches, and spirits, and where humans are changed into beasts.",
                "https://images.unsplash.com/photo-1578632767115-351597cf2477?auto=format&fit=crop&w=600&q=80",
                "https://www.youtube.com/embed/ByXuk9QqQkk"
        );
        movieRepository.save(spiritedAway);

        // Movie 6: Oppenheimer
        Movie oppenheimer = new Movie(
                "Oppenheimer",
                "Drama",
                2023,
                "Christopher Nolan",
                "Cillian Murphy, Emily Blunt, Matt Damon, Robert Downey Jr.",
                180,
                "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb during World War II.",
                "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?auto=format&fit=crop&w=600&q=80",
                "https://www.youtube.com/embed/uYPbbksJxIg"
        );
        movieRepository.save(oppenheimer);

        // Add sample reviews
        if (john != null) {
            reviewRepository.save(new Review(john, inception, 5, "An absolute cinematic masterpiece! The concept of dreams within dreams and the Hans Zimmer soundtrack gave me chills.", ReviewStatus.APPROVED));
            reviewRepository.save(new Review(john, darkKnight, 5, "Heath Ledger's Joker performance is unmatched. One of the greatest superhero thrillers ever created.", ReviewStatus.APPROVED));
            reviewRepository.save(new Review(john, interstellar, 4, "Emotionally gripping and scientifically fascinating. The visual effects of gargantua were breathtaking.", ReviewStatus.APPROVED));
        }

        if (sarah != null) {
            reviewRepository.save(new Review(sarah, inception, 4, "Brilliant storyline, though it requires multiple viewings to fully digest every detail.", ReviewStatus.APPROVED));
            reviewRepository.save(new Review(sarah, spiritedAway, 5, "A gorgeous, poetic animated journey. Hayao Miyazaki's art style is timeless.", ReviewStatus.APPROVED));
            reviewRepository.save(new Review(sarah, oppenheimer, 5, "Incredible pacing for a 3-hour biographical drama. Cillian Murphy and Robert Downey Jr deliver career-best performances.", ReviewStatus.APPROVED));
        }

        if (admin != null) {
            reviewRepository.save(new Review(admin, pulpFiction, 5, "Sharp non-linear storytelling with iconic dialogues. Tarantino at his best.", ReviewStatus.APPROVED));
        }

        // Update average ratings for seeded movies
        movieService.updateMovieRating(inception.getId());
        movieService.updateMovieRating(darkKnight.getId());
        movieService.updateMovieRating(interstellar.getId());
        movieService.updateMovieRating(pulpFiction.getId());
        movieService.updateMovieRating(spiritedAway.getId());
        movieService.updateMovieRating(oppenheimer.getId());
    }
}
