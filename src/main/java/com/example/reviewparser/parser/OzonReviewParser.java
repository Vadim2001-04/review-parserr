package com.example.reviewparser.parser;

import com.example.reviewparser.model.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class OzonReviewParser {

    private final Random random = new Random();

    public List<Review> parseReviews(String productUrl) {
        log.info("Парсинг URL: {}", productUrl);

        try {
            Thread.sleep(500 + random.nextInt(1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return List.of();
        }

        List<Review> reviews = new ArrayList<>();
        int count = 2 + random.nextInt(3);

        for (int i = 0; i < count; i++) {
            Review r = new Review();
            r.setAuthor("Пользователь " + (1000 + random.nextInt(9000)));
            r.setText("Товар понравился! Качество отличное. Доставка быстрая.");
            r.setRating(4 + random.nextInt(2));
            r.setDate(LocalDateTime.now().minusDays(random.nextInt(10)));
            r.setSource("Ozon");
            r.setProductUrl(productUrl);
            reviews.add(r);
        }

        log.info("Собрано {} отзывов с {}", reviews.size(), productUrl);
        return reviews;
    }
}