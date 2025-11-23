package com.example.reviewparser.controller;

import com.example.reviewparser.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/parse")
    public ResponseEntity<String> parseReviews(@RequestBody List<String> urls) {
        if (urls == null || urls.isEmpty()) {
            return ResponseEntity.badRequest().body("Список URL пуст");
        }
        reviewService.parseReviewsFromUrls(urls);
        return ResponseEntity.ok("Парсинг запущен для " + urls.size() + " URL");
    }

    @GetMapping
    public ResponseEntity<List<?>> getReviews(
            @RequestParam(defaultValue = "0") int minRating,
            @RequestParam(defaultValue = "rating") String sortBy) {
        List<?> reviews = reviewService.getReviews(minRating, sortBy);
        return ResponseEntity.ok(reviews);
    }
}