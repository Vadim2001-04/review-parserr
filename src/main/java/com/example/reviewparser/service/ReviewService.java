package com.example.reviewparser.service;

import com.example.reviewparser.model.Review;
import com.example.reviewparser.parser.OzonReviewParser;
import com.example.reviewparser.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OzonReviewParser ozonReviewParser;
    private final ExecutorService executorService;

    public ReviewService(ReviewRepository reviewRepository,
                         OzonReviewParser ozonReviewParser,
                         @Qualifier("reviewParserExecutor") ExecutorService executorService) {
        this.reviewRepository = reviewRepository;
        this.ozonReviewParser = ozonReviewParser;
        this.executorService = executorService;
    }

    private final ConcurrentLinkedQueue<Review> tempQueue = new ConcurrentLinkedQueue<>();

    public void parseReviewsFromUrls(List<String> urls) {
        log.info("Запуск парсинга {} URL", urls.size());
        tempQueue.clear();

        List<Future<?>> futures = new ArrayList<>();
        for (String url : urls) {
            Future<?> future = executorService.submit(() -> {
                List<Review> reviews = ozonReviewParser.parseReviews(url);
                tempQueue.addAll(reviews);
            });
            futures.add(future);
        }

        // Ждём завершения
        futures.forEach(f -> {
            try {
                f.get();
            } catch (Exception e) {
                log.error("Ошибка при парсинге", e);
            }
        });

        // Сохраняем в БД
        List<Review> toSave = new ArrayList<>(tempQueue);
        reviewRepository.saveAll(toSave);
        log.info("Сохранено {} отзывов в БД", toSave.size());
    }

    public List<Review> getReviews(int minRating, String sortBy) {
        List<Review> all = reviewRepository.findAll();

        return all.stream()
                .parallel() // parallelStream
                .filter(r -> r.getRating() >= minRating)
                .sorted((r1, r2) -> {
                    if ("date".equalsIgnoreCase(sortBy)) {
                        return r2.getDate().compareTo(r1.getDate());
                    } else {
                        return Integer.compare(r2.getRating(), r1.getRating());
                    }
                })
                .collect(Collectors.toList());
    }
}