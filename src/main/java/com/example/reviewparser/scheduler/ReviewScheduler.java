package com.example.reviewparser.scheduler;

import com.example.reviewparser.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ReviewScheduler {

    private final ReviewService reviewService;

    public ReviewScheduler(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Scheduled(fixedRate = 1800000) // 30 минут
    public void scheduledParse() {
        log.info("Запуск планового парсинга... (поток: {})", Thread.currentThread().getName());
        List<String> urls = List.of(
                "https://www.ozon.ru/product/emulated-1",
                "https://www.ozon.ru/product/emulated-2"
        );
        reviewService.parseReviewsFromUrls(urls);
        log.info("Плановый парсинг завершён.");
    }
}