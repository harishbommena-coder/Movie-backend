package com.moviereview.dto;

import com.moviereview.entity.Movie;
import java.time.LocalDateTime;

public class MovieDto {
    private Long id;
    private String title;
    private String genre;
    private Integer releaseYear;
    private String director;
    private String cast;
    private Integer durationMinutes;
    private String synopsis;
    private String posterUrl;
    private String trailerUrl;
    private Double averageRating;
    private Integer totalReviews;
    private LocalDateTime createdAt;

    public MovieDto() {
    }

    public MovieDto(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.genre = movie.getGenre();
        this.releaseYear = movie.getReleaseYear();
        this.director = movie.getDirector();
        this.cast = movie.getCast();
        this.durationMinutes = movie.getDurationMinutes();
        this.synopsis = movie.getSynopsis();
        this.posterUrl = movie.getPosterUrl();
        this.trailerUrl = movie.getTrailerUrl();
        this.averageRating = movie.getAverageRating() != null ? Math.round(movie.getAverageRating() * 10.0) / 10.0 : 0.0;
        this.totalReviews = movie.getTotalReviews() != null ? movie.getTotalReviews() : 0;
        this.createdAt = movie.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
