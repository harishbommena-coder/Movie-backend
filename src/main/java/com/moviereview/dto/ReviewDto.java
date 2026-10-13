package com.moviereview.dto;

import com.moviereview.entity.Review;
import java.time.LocalDateTime;

public class ReviewDto {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long userId;
    private String username;
    private Integer rating;
    private String comment;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewDto() {
    }

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.movieId = review.getMovie() != null ? review.getMovie().getId() : null;
        this.movieTitle = review.getMovie() != null ? review.getMovie().getTitle() : null;
        this.userId = review.getUser() != null ? review.getUser().getId() : null;
        this.username = review.getUser() != null ? review.getUser().getUsername() : null;
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.status = review.getStatus() != null ? review.getStatus().name() : null;
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
