package com.example.reviewparser.service;

import com.example.reviewparser.model.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Test
    void shouldSortReviewsByRating() {
        Review r1 = new Review(null, "User1", "Good", 5, null, "Ozon", "url1");
        Review r2 = new Review(null, "User2", "Bad", 3, null, "Ozon", "url2");
        Review r3 = new Review(null, "User3", "Ok", 4, null, "Ozon", "url3");

        List<Review> reviews = List.of(r1, r2, r3);

        List<Review> filtered = reviews.stream()
                .parallel()
                .filter(r -> r.getRating() >= 4)
                .sorted((a, b) -> Integer.compare(b.getRating(), a.getRating()))
                .collect(Collectors.toList());

        assertEquals(2, filtered.size());
        assertEquals(5, filtered.get(0).getRating());
    }
}