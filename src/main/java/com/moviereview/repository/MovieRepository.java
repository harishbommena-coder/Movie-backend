package com.moviereview.repository;

import com.moviereview.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByGenreIgnoreCase(String genre);

    @Query("SELECT m FROM Movie m WHERE " +
           "(:search IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(m.director) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(m.genre) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:genre IS NULL OR LOWER(m.genre) LIKE LOWER(CONCAT('%', :genre, '%')))")
    List<Movie> searchMovies(@Param("search") String search, @Param("genre") String genre);

    @Query("SELECT DISTINCT m.genre FROM Movie m")
    List<String> findDistinctGenres();
}
