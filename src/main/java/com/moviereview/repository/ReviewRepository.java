package com.moviereview.repository;

import com.moviereview.entity.Review;
import com.moviereview.entity.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMovieIdAndStatusOrderByCreatedAtDesc(Long movieId, ReviewStatus status);

    List<Review> findByMovieIdOrderByCreatedAtDesc(Long movieId);

    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Review> findByMovieIdAndUserId(Long movieId, Long userId);

    List<Review> findByStatusOrderByCreatedAtDesc(ReviewStatus status);

    List<Review> findAllByOrderByCreatedAtDesc();

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.movie.id = :movieId AND r.status = :status")
    Double calculateAverageRating(@Param("movieId") Long movieId, @Param("status") ReviewStatus status);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.movie.id = :movieId AND r.status = :status")
    Integer countReviewsByStatus(@Param("movieId") Long movieId, @Param("status") ReviewStatus status);

    long countByStatus(ReviewStatus status);
}
