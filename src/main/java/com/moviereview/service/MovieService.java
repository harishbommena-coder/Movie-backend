package com.moviereview.service;

import com.moviereview.dto.MovieDto;
import com.moviereview.dto.MovieRequest;
import com.moviereview.entity.Movie;
import com.moviereview.entity.ReviewStatus;
import com.moviereview.repository.MovieRepository;
import com.moviereview.repository.ReviewRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    public MovieService(MovieRepository movieRepository, @Lazy ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<MovieDto> getAllMovies(String search, String genre) {
        String cleanSearch = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        String cleanGenre = (genre != null && !genre.trim().isEmpty() && !genre.equalsIgnoreCase("All")) ? genre.trim() : null;

        List<Movie> movies;
        if (cleanSearch == null && cleanGenre == null) {
            movies = movieRepository.findAll();
        } else {
            movies = movieRepository.searchMovies(cleanSearch, cleanGenre);
        }

        return movies.stream()
                .map(MovieDto::new)
                .collect(Collectors.toList());
    }

    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));
        return new MovieDto(movie);
    }

    public Movie getMovieEntity(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));
    }

    @Transactional
    public MovieDto createMovie(MovieRequest request) {
        Movie movie = new Movie(
                request.getTitle().trim(),
                request.getGenre().trim(),
                request.getReleaseYear(),
                request.getDirector() != null ? request.getDirector().trim() : "",
                request.getCast() != null ? request.getCast().trim() : "",
                request.getDurationMinutes(),
                request.getSynopsis() != null ? request.getSynopsis().trim() : "",
                request.getPosterUrl() != null ? request.getPosterUrl().trim() : "",
                request.getTrailerUrl() != null ? request.getTrailerUrl().trim() : ""
        );
        Movie saved = movieRepository.save(movie);
        return new MovieDto(saved);
    }

    @Transactional
    public MovieDto updateMovie(Long id, MovieRequest request) {
        Movie movie = getMovieEntity(id);

        movie.setTitle(request.getTitle().trim());
        movie.setGenre(request.getGenre().trim());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setDirector(request.getDirector() != null ? request.getDirector().trim() : "");
        movie.setCast(request.getCast() != null ? request.getCast().trim() : "");
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setSynopsis(request.getSynopsis() != null ? request.getSynopsis().trim() : "");
        movie.setPosterUrl(request.getPosterUrl() != null ? request.getPosterUrl().trim() : "");
        movie.setTrailerUrl(request.getTrailerUrl() != null ? request.getTrailerUrl().trim() : "");

        Movie updated = movieRepository.save(movie);
        return new MovieDto(updated);
    }

    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = getMovieEntity(id);
        movieRepository.delete(movie);
    }

    public List<String> getAllGenres() {
        return movieRepository.findDistinctGenres();
    }

    @Transactional
    public void updateMovieRating(Long movieId) {
        Movie movie = getMovieEntity(movieId);
        Double avg = reviewRepository.calculateAverageRating(movieId, ReviewStatus.APPROVED);
        Integer count = reviewRepository.countReviewsByStatus(movieId, ReviewStatus.APPROVED);

        movie.setAverageRating(avg != null ? avg : 0.0);
        movie.setTotalReviews(count != null ? count : 0);
        movieRepository.save(movie);
    }

    public long countMovies() {
        return movieRepository.count();
    }
}
