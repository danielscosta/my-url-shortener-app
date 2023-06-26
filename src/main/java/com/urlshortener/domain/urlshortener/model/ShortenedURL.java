package com.urlshortener.domain.urlshortener.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Entity
public class ShortenedURL {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, updatable = false)
    @Pattern(regexp = "^(https?|ftp)://[^ /$.?#].[^ ]*$", message = "Invalid URL format")
    private String originalURL;

    @Column(nullable = false, unique = true, updatable = false)
    private String shortCode;

    @Column(nullable = false)
    private Integer shortenedTimes = 1;

    @Column(nullable = false)
    private Integer accessedTimes = 0;

    @Column(nullable = false, updatable = false)
    private String username;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Integer getShortenedTimes() {
        return shortenedTimes;
    }

    public void setShortenedTimes(Integer shortenedTimes) {
        this.shortenedTimes = shortenedTimes;
    }

    public Integer getAccessedTimes() {
        return accessedTimes;
    }

    public void setAccessedTimes(Integer accessedTimes) {
        this.accessedTimes = accessedTimes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
