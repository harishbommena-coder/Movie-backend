package com.moviereview.dto;

public class StatsDto {
    private long totalMovies;
    private long totalReviews;
    private long totalUsers;
    private long pendingReviews;
    private double platformAverageRating;

    public StatsDto() {
    }

    public StatsDto(long totalMovies, long totalReviews, long totalUsers, long pendingReviews, double platformAverageRating) {
        this.totalMovies = totalMovies;
        this.totalReviews = totalReviews;
        this.totalUsers = totalUsers;
        this.pendingReviews = pendingReviews;
        this.platformAverageRating = platformAverageRating;
    }

    public long getTotalMovies() {
        return totalMovies;
    }

    public void setTotalMovies(long totalMovies) {
        this.totalMovies = totalMovies;
    }

    public long getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(long totalReviews) {
        this.totalReviews = totalReviews;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getPendingReviews() {
        return pendingReviews;
    }

    public void setPendingReviews(long pendingReviews) {
        this.pendingReviews = pendingReviews;
    }

    public double getPlatformAverageRating() {
        return platformAverageRating;
    }

    public void setPlatformAverageRating(double platformAverageRating) {
        this.platformAverageRating = platformAverageRating;
    }
}
