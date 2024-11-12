package com.reviewping.coflo.domain.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.domain.notification.repository.EmitterRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseService {
    private static final long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final ObjectMapper objectMapper;

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = createEmitter(userId);

        sendToClient(userId, "EventStream Created. [userId=" + userId + "]");
        return emitter;
    }

    public void notify(Long userId, Object event) {
        System.out.println("Notify method called with userId: " + userId + " and event: " + event);
        sendToClient(userId, event);
    }

    private void sendToClient(Long id, Object data) {
        SseEmitter emitter = emitterRepository.get(id);
        if (emitter != null) {
            try {
                String jsonData = objectMapper.writeValueAsString(data);
                System.out.println("Sending data: " + jsonData);
                emitter.send(
                        SseEmitter.event()
                                .id(String.valueOf(id))
                                .name("notification")
                                .data(jsonData));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException("JSON 변환 오류!", e);
            } catch (IOException e) {
                e.printStackTrace();
                emitterRepository.deleteById(id);
                throw new RuntimeException("연결 오류!", e);
            }
        } else {
            System.err.println("Emitter not found for id: " + id);
        }
    }

    private SseEmitter createEmitter(Long id) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(id, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));
        return emitter;
    }
}
