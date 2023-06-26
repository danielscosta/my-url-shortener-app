package com.urlshortener.domain.urlshortener.service;

import com.urlshortener.domain.urlshortener.model.ShortenedURL;
import com.urlshortener.domain.urlshortener.repository.URLShortenerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class URLShortenerServiceTest {

    @Mock
    private URLShortenerRepository urlShortenerRepository;

    @InjectMocks
    private URLShortenerService urlShortenerService;

    private UserDetails adminUser;
    private UserDetails regularUser;

    @BeforeEach
    void setup() {
        adminUser = User.withUsername("admin")
                .password("password")
                .roles("ADMIN")
                .build();

        regularUser = User.withUsername("john")
                .password("password")
                .roles("USER")
                .build();
    }

    @Test
    void findAllByUsername_ReturnsAllURLsForAdminUser() {
        // Mocked repository response
        List<ShortenedURL> expectedURLs = Arrays.asList(
                createShortenedURL("https://example.com", "abc123", "admin", 2, 5),
                createShortenedURL("https://google.com", "def456", "admin", 1, 3)
        );
        when(urlShortenerRepository.findAll()).thenReturn(expectedURLs);

        // Invoke the service method
        List<ShortenedURL> actualURLs = urlShortenerService.findAllByUsername(adminUser);

        // Verify the repository method was called
        verify(urlShortenerRepository, times(1)).findAll();

        // Verify the returned URLs
        assertEquals(expectedURLs, actualURLs);
    }

    @Test
    void findAllByUsername_ReturnsUserSpecificURLsForNonAdminUser() {
        // Mocked repository response
        List<ShortenedURL> expectedURLs = Arrays.asList(
                createShortenedURL("https://example.com", "abc123", "john", 2, 5),
                createShortenedURL("https://google.com", "def456", "john", 1, 3)
        );
        when(urlShortenerRepository.findAllByUsername("john")).thenReturn(expectedURLs);

        // Invoke the service method
        List<ShortenedURL> actualURLs = urlShortenerService.findAllByUsername(regularUser);

        // Verify the repository method was called
        verify(urlShortenerRepository, times(1)).findAllByUsername("john");

        // Verify the returned URLs
        assertEquals(expectedURLs, actualURLs);
    }

    @Test
    void shortenURL_CreatesNewShortenedURLForUniqueOriginalURL() {
        // Mocked repository response
        when(urlShortenerRepository.findByOriginalURLAndUsername("https://example.com", "john")).thenReturn(Optional.empty());

        // Invoke the service method
        urlShortenerService.shortenURL("https://example.com", "john");

        // Verify the repository method was called to save the new entity
        verify(urlShortenerRepository, times(1)).save(any(ShortenedURL.class));
    }

    @Test
    void shortenURL_IncrementsShortenedTimesForExistingOriginalURL() {
        // Mocked repository response
        ShortenedURL existingURL = createShortenedURL("https://example.com", "abc123", "john", 1, 3);
        when(urlShortenerRepository.findByOriginalURLAndUsername("https://example.com", "john")).thenReturn(Optional.of(existingURL));

        // Invoke the service method
        urlShortenerService.shortenURL("https://example.com", "john");

        // Verify the repository method was called to save the existing entity
        verify(urlShortenerRepository, times(1)).save(existingURL);
    }

    @Test
    void getOriginalURL_ReturnsOriginalURLForValidShortCode() {
        // Mocked repository response
        ShortenedURL existingURL = createShortenedURL("https://example.com", "abc123", "john", 2, 5);
        when(urlShortenerRepository.findByShortCodeAndUsername("abc123", "john")).thenReturn(Optional.of(existingURL));

        // Invoke the service method
        String originalURL = urlShortenerService.getOriginalURL("abc123", "john");

        // Verify the repository method was called to save the existing entity
        verify(urlShortenerRepository, times(1)).save(existingURL);

        // Verify the returned original URL
        assertEquals("https://example.com", originalURL);
    }

    @Test
    void getOriginalURL_ReturnsNullForInvalidShortCode() {
        // Mocked repository response
        when(urlShortenerRepository.findByShortCodeAndUsername("invalid", "john")).thenReturn(Optional.empty());

        // Invoke the service method
        String originalURL = urlShortenerService.getOriginalURL("invalid", "john");

        // Verify the repository method was not called
        verify(urlShortenerRepository, never()).save(any(ShortenedURL.class));

        // Verify the returned original URL is null
        assertNull(originalURL);
    }

    private ShortenedURL createShortenedURL(String originalURL, String shortCode, String username, int shortenedTimes, int accessedTimes) {
        ShortenedURL url = new ShortenedURL();
        url.setOriginalURL(originalURL);
        url.setShortCode(shortCode);
        url.setUsername(username);
        url.setShortenedTimes(shortenedTimes);
        url.setAccessedTimes(accessedTimes);
        return url;
    }
}
