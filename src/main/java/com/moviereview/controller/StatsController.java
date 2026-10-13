package com.moviereview.controller;

import com.moviereview.dto.StatsDto;
import com.moviereview.service.MovieService;
import com.moviereview.service.ReviewService;
import com.moviereview.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/stats")
public class StatsController {

    private final MovieService movieService;
    private final ReviewService reviewService;
    private final UserService userService;

    public StatsController(MovieService movieService, ReviewService reviewService, UserService userService) {
        this.movieService = movieService;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StatsDto> getDashboardStats() {
        long totalMovies = movieService.countMovies();
        long totalReviews = reviewService.countReviews();
        long totalUsers = userService.countUsers();
        long pendingReviews = reviewService.countPendingReviews();

        // Calculate average across movies
        double avg = 0.0;
        var movies = movieService.getAllMovies(null, null);
        if (!movies.isEmpty()) {
            double sum = movies.stream().mapToDouble(m -> m.getAverageRating() != null ? m.getAverageRating() : 0.0).sum();
            long ratedCount = movies.stream().filter(m -> m.getTotalReviews() > 0).count();
            if (ratedCount > 0) {
                avg = Math.round((sum / ratedCount) * 10.0) / 10.0;
            }
        }

        StatsDto stats = new StatsDto(totalMovies, totalReviews, totalUsers, pendingReviews, avg);
        return ResponseEntity.ok(stats);
    }
}
