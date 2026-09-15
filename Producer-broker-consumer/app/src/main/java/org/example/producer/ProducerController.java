package org.example.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ProducerController {
    private final KafkaTemplate<String, CoursePurchase> kafkaTemplate;

    ProducerController(KafkaTemplate<String, CoursePurchase> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/course")
    String send(@RequestBody CoursePurchase purchase) {

        kafkaTemplate.send(
                "course-events",purchase);

        return "Purchase received";
    }
}
