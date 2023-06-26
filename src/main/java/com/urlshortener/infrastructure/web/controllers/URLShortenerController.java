package com.urlshortener.infrastructure.web.controllers;

import com.urlshortener.domain.urlshortener.model.ShortenedURL;
import com.urlshortener.domain.urlshortener.service.URLShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class URLShortenerController {

    @Autowired
    private URLShortenerService urlShortenerService;

    @GetMapping("/shortened_urls")
    public ResponseEntity<List<ShortenedURL>> shortenedURLs() {
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(urlShortenerService.findAllByUsername(userDetails));
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenURL(@RequestParam String originalURL, HttpServletResponse response) throws IOException {
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        urlShortenerService.shortenURL(originalURL, userDetails.getUsername());
        response.sendRedirect("/");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/shortCode")
    public ResponseEntity<Void> redirectToOriginalURL(@RequestParam String value, HttpServletResponse response) throws IOException {
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String originalURL = urlShortenerService.getOriginalURL(value, userDetails.getUsername());
        if (originalURL != null) {
            response.sendRedirect(originalURL);
            return ResponseEntity.status(HttpStatus.FOUND).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
