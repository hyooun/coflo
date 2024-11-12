package com.reviewping.coflo.domain.notification.api;

import com.reviewping.coflo.domain.notification.service.SseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {
    private final SseService sseService;

    @Operation(summary = "SSE 연결", description = "SSE를 연결합니다.")
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam(name = "mrInfoId") Long mrInfoId) {
        return sseService.subscribe(mrInfoId);
    }

    @PostMapping
    public void sendDataTest(@RequestParam(name = "mrInfoId") Long mrInfoId) {
        sseService.notify(mrInfoId, "data");
    }
}
