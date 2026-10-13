package com.moviereview.controller;

import com.moviereview.dto.ReviewDto;
import com.moviereview.dto.ReviewRequest;
import com.moviereview.entity.User;
import com.moviereview.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Get public approved reviews for a movie
    @GetMapping("/movies/{movieId}/reviews")
    public ResponseEntity<List<ReviewDto>> getMovieReviews(@PathVariable Long movieId) {
        return ResponseEntity.ok(reviewService.getApprovedReviewsForMovie(movieId));
    }

    // Get current user's review for a movie if exists
    @GetMapping("/movies/{movieId}/my-review")
    public ResponseEntity<?> getMyMovieReview(
            @PathVariable Long movieId,
            @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not authenticated"));
        }
        return reviewService.getUserReviewForMovie(movieId, user.getId())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    // Submit review & rating
    @PostMapping("/movies/{movieId}/reviews")
    public ResponseEntity<?> submitReview(
            @PathVariable Long movieId,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ReviewRequest request) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Please login to write a review"));
        }
        try {
            ReviewDto review = reviewService.submitReview(movieId, user, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(review);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // Edit an existing review
    @PutMapping("/reviews/{id}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ReviewRequest request) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not authenticated"));
        }
        try {
            ReviewDto updated = reviewService.updateReview(id, user, request);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
        }
    }

    // Delete review
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not authenticated"));
        }
        try {
            reviewService.deleteReview(id, user);
            return ResponseEntity.ok(Map.of("message", "Review deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
        }
    }

    // Admin endpoints for Review moderation
    @GetMapping("/admin/reviews")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<ReviewDto>> getAllReviewsAdmin() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/admin/reviews/pending")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<ReviewDto>> getPendingReviewsAdmin() {
        return ResponseEntity.ok(reviewService.getPendingReviews());
    }

    @PostMapping("/admin/reviews/{id}/approve")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> approveReview(@PathVariable Long id) {
        try {
            ReviewDto approved = reviewService.approveReview(id);
            return ResponseEntity.ok(approved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/admin/reviews/{id}/reject")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> rejectReview(@PathVariable Long id) {
        try {
            ReviewDto rejected = reviewService.rejectReview(id);
            return ResponseEntity.ok(rejected);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }
}
