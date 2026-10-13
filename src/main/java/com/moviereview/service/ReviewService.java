package com.moviereview.service;

import com.moviereview.dto.ReviewDto;
import com.moviereview.dto.ReviewRequest;
import com.moviereview.entity.Movie;
import com.moviereview.entity.Review;
import com.moviereview.entity.ReviewStatus;
import com.moviereview.entity.Role;
import com.moviereview.entity.User;
import com.moviereview.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieService movieService;

    public ReviewService(ReviewRepository reviewRepository, MovieService movieService) {
        this.reviewRepository = reviewRepository;
        this.movieService = movieService;
    }

    public List<ReviewDto> getApprovedReviewsForMovie(Long movieId) {
        return reviewRepository.findByMovieIdAndStatusOrderByCreatedAtDesc(movieId, ReviewStatus.APPROVED)
                .stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getAllReviewsForMovie(Long movieId) {
        return reviewRepository.findByMovieIdOrderByCreatedAtDesc(movieId)
                .stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getPendingReviews() {
        return reviewRepository.findByStatusOrderByCreatedAtDesc(ReviewStatus.PENDING)
                .stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    public Optional<ReviewDto> getUserReviewForMovie(Long movieId, Long userId) {
        return reviewRepository.findByMovieIdAndUserId(movieId, userId)
                .map(ReviewDto::new);
    }

    @Transactional
    public ReviewDto submitReview(Long movieId, User user, ReviewRequest request) {
        Movie movie = movieService.getMovieEntity(movieId);

        // Check if user already reviewed this movie
        Optional<Review> existing = reviewRepository.findByMovieIdAndUserId(movieId, user.getId());
        Review review;

        if (existing.isPresent()) {
            review = existing.get();
            review.setRating(request.getRating());
            review.setComment(request.getComment().trim());
            review.setUpdatedAt(LocalDateTime.now());
            // Kept approved or re-evaluated
            review.setStatus(ReviewStatus.APPROVED);
        } else {
            review = new Review(
                    user,
                    movie,
                    request.getRating(),
                    request.getComment().trim(),
                    ReviewStatus.APPROVED
            );
        }

        Review saved = reviewRepository.save(review);
        movieService.updateMovieRating(movieId);

        return new ReviewDto(saved);
    }

    @Transactional
    public ReviewDto updateReview(Long reviewId, User user, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + reviewId));

        // Check permissions: author or admin
        boolean isAuthor = review.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Role.ROLE_ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new IllegalStateException("You are not authorized to edit this review");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment().trim());
        review.setUpdatedAt(LocalDateTime.now());

        Review updated = reviewRepository.save(review);
        movieService.updateMovieRating(review.getMovie().getId());

        return new ReviewDto(updated);
    }

    @Transactional
    public void deleteReview(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + reviewId));

        boolean isAuthor = review.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Role.ROLE_ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new IllegalStateException("You are not authorized to delete this review");
        }

        Long movieId = review.getMovie().getId();
        reviewRepository.delete(review);
        movieService.updateMovieRating(movieId);
    }

    @Transactional
    public ReviewDto approveReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + reviewId));

        review.setStatus(ReviewStatus.APPROVED);
        Review saved = reviewRepository.save(review);
        movieService.updateMovieRating(review.getMovie().getId());

        return new ReviewDto(saved);
    }

    @Transactional
    public ReviewDto rejectReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + reviewId));

        review.setStatus(ReviewStatus.REJECTED);
        Review saved = reviewRepository.save(review);
        movieService.updateMovieRating(review.getMovie().getId());

        return new ReviewDto(saved);
    }

    public long countReviews() {
        return reviewRepository.count();
    }

    public long countPendingReviews() {
        return reviewRepository.countByStatus(ReviewStatus.PENDING);
    }
}
