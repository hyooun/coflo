package com.reviewping.coflo.domain.notification.service;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.domain.notification.repository.EmitterRepository;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {
    private static final long DEFAULT_TIMEOUT = 60L * 1000 * 3; // 3분

    private final EmitterRepository emitterRepository;
    private final ObjectMapper objectMapper;

    public SseEmitter subscribe(Long reviewId) {
        SseEmitter emitter = createEmitter(reviewId);
        sendToClient(reviewId, "EventStream Created. [reviewId=" + reviewId + "]");
        return emitter;
    }

    public void notify(Long reviewId, Object event) {
        log.info("Notify method called with reviewId: " + reviewId + " and event: " + event);
        sendToClient(reviewId, event);
    }

    private void sendToClient(Long id, Object data) {
        SseEmitter emitter = emitterRepository.get(id);
        if (emitter != null) {
            try {
                String jsonData = objectMapper.writeValueAsString(data);
                log.info("Sending data: " + jsonData);
                emitter.send(
                        SseEmitter.event()
                                .id(String.valueOf(id))
                                .name("notification")
                                .data(jsonData));
            } catch (JsonProcessingException e) {
                throw new BusinessException(SSE_DATA_SERIALIZATION_ERROR);
            } catch (IOException e) {
                emitterRepository.deleteById(id);
                throw new BusinessException(SSE_DATA_SEND_ERROR);
            }
        } else {
            log.error("Emitter not found for reviewId: " + id);
        }
    }

    private SseEmitter createEmitter(Long id) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(id, emitter);

        emitter.onCompletion(
                () -> {
                    log.info("SSE connection completed for id: " + id);
                    emitterRepository.deleteById(id);
                });
        emitter.onTimeout(
                () -> {
                    log.info("SSE connection timed out for id: " + id);
                    emitter.complete();
                    emitterRepository.deleteById(id);
                });
        emitter.onError(
                (e) -> {
                    log.error("SSE connection error for id: " + id, e);
                    emitterRepository.deleteById(id);
                });

        return emitter;
    }
}
