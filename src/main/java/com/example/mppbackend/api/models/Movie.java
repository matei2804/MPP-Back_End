package com.example.mppbackend.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Movie")
public class Movie {

    @Id
    private String id;
    @Column(name = "Title", nullable = false)
    private String title;
    @Column(name = "Genre", nullable = false)
    private String genre;
    @Column(name = "YearOfRelease", nullable = false)
    private int yearOfRelease;
    @Column(name = "TrailerLink", nullable = false)
    private String trailerLink;
    @Column(name = "Photo", nullable = false)
    private String photo;
    @Column(name = "UserId", nullable = false)
    private String userId;

    public Movie(String id, String title, String genre, int yearOfRelease, String trailerLink, String photo, String userId) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.yearOfRelease = yearOfRelease;
        this.trailerLink = trailerLink;
        this.photo = photo;
        this.userId = userId;
    }

    public Movie() {

    }

    @JsonProperty("userId")
    public String getUserID() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    @JsonProperty("photo")
    public String getPhoto() {
        return photo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setYearOfRelease(int yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setUserID(String userId) {
        this.userId = userId;
    }
}
