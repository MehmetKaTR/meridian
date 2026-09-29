package org.mehmetkatr.meridian.payment.outbox;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mehmetkatr.meridian.payment.entity.OutboxEvent;
import org.mehmetkatr.meridian.payment.entity.OutboxStatus;
import org.mehmetkatr.meridian.payment.repository.OutboxRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxProcessor {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPending() {
        List<OutboxEvent> pending = outboxRepository.findByStatus(OutboxStatus.PENDING);

        for (OutboxEvent e : pending) {
            try {
                kafkaTemplate.send("payment-events", e.getPayload()).get();
                e.setStatus(OutboxStatus.SENT);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                log.warn("Outbox publish interrupted, will retry next run. eventId={}", e.getId());
                break;
            } catch (Exception ex) {
                log.warn("Outbox publish failed, will retry next run. eventId={}", e.getId(), ex);
                break;
            }
        }
    }
}