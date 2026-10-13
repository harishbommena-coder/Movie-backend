package com.moviereview.controller;

import com.moviereview.dto.MovieDto;
import com.moviereview.dto.MovieRequest;
import com.moviereview.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre) {
        return ResponseEntity.ok(movieService.getAllMovies(search, genre));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(movieService.getMovieById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/genres")
    public ResponseEntity<List<String>> getAllGenres() {
        return ResponseEntity.ok(movieService.getAllGenres());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createMovie(@Valid @RequestBody MovieRequest request) {
        try {
            MovieDto created = movieService.createMovie(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest request) {
        try {
            MovieDto updated = movieService.updateMovie(id, request);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        try {
            movieService.deleteMovie(id);
            return ResponseEntity.ok(Map.of("message", "Movie deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }
}
