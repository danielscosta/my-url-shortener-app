package com.urlshortener.domain.urlshortener.repository;

import com.urlshortener.domain.urlshortener.model.ShortenedURL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface URLShortenerRepository extends JpaRepository<ShortenedURL, UUID> {

    Optional<ShortenedURL> findByShortCodeAndUsername(String shortCode, String username);

    Optional<ShortenedURL> findByOriginalURLAndUsername(String originalURL, String username);

    List<ShortenedURL> findAllByUsername(String username);
}
