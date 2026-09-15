package org.example.consumer;

import org.example.producer.CoursePurchase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;

@Service
public class Consumer {

    private final Counter purchaseCounter;

    public Consumer(MeterRegistry meterRegistry) {
        purchaseCounter = Counter.builder("course_purchase_total")
                .description("Total number of course purchases")
                .register(meterRegistry);
    }

    @KafkaListener(topics = "course-events", groupId = "course-consumer-group")
    public void consume(CoursePurchase purchase) {
        System.out.println("Course: " + purchase.getCourse());
        System.out.println("Student: " + purchase.getStudent());
        purchaseCounter.increment();
    }
}