package com.urlshortener.domain.urlshortener.service;

import com.urlshortener.domain.urlshortener.model.ShortenedURL;
import com.urlshortener.domain.urlshortener.repository.URLShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class URLShortenerService {

    @Autowired
    private URLShortenerRepository urlShortenerRepository;

    public List<ShortenedURL> findAllByUsername(UserDetails userDetails) {
        if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            return urlShortenerRepository.findAll();
        } else {
            return urlShortenerRepository.findAllByUsername(userDetails.getUsername());
        }
    }

    public void shortenURL(String originalURL, String username) {
        Optional<ShortenedURL> optEntity = urlShortenerRepository.findByOriginalURLAndUsername(originalURL, username);
        ShortenedURL entity;
        if (optEntity.isEmpty()) {
            String shortCode = generateShortCode();
            entity = new ShortenedURL();
            entity.setOriginalURL(originalURL);
            entity.setShortCode(shortCode);
            entity.setUsername(username);
        } else {
            entity = optEntity.get();
            entity.setShortenedTimes(entity.getShortenedTimes() + 1);
        }
        urlShortenerRepository.save(entity);
    }

    public String getOriginalURL(String shortCode, String username) {
        Optional<ShortenedURL> optionalEntity = urlShortenerRepository.findByShortCodeAndUsername(shortCode, username);
        if (!optionalEntity.isEmpty()) {
            ShortenedURL entity = optionalEntity.get();
            entity.setAccessedTimes(entity.getAccessedTimes() + 1);
            urlShortenerRepository.save(entity);
        }
        return optionalEntity.map(ShortenedURL::getOriginalURL).orElse(null);
    }

    private String generateShortCode() {
        // Generate a unique short code using any logic you prefer (e.g., hashing, encoding, random string, etc.)
        // This is just a simple example using a random alphanumeric code of length 6
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
