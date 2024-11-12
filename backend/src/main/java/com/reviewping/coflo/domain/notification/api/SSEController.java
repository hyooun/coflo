package com.reviewping.coflo.domain.notification.api;

import com.reviewping.coflo.domain.notification.service.SSEService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SSEController {
    private final SSEService sseService;

    @Operation(summary = "SSE 연결", description = "SSE를 연결합니다.")
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthUser User user) {
        return sseService.subscribe(user.getId());
    }

    @PostMapping
    public void sendDataTest(@AuthUser User user) {
        sseService.notify(user.getId(), "data");
    }
}
