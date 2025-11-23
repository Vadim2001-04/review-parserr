package com.example.reviewparser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SimpleThreadExample implements Runnable {

    private int counter = 0;

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            counter++;
            log.info("Поток {} - шаг {}", Thread.currentThread().getName(), counter);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void startInNewThread() {
        Thread thread = new Thread(this, "SimpleDemoThread");
        thread.start();
    }
}