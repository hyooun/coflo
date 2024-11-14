package com.reviewping.coflo.domain.notification.api;

import com.reviewping.coflo.domain.notification.service.SseService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
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
    public SseEmitter subscribe(@AuthUser User user) {
        return sseService.subscribe(user.getId());
    }

    @PostMapping("/disconnect")
    public ApiResponse<Void> disconnect(@RequestParam(name = "reviewId") Long reviewId) {
        sseService.disconnect(reviewId);
        return ApiSuccessResponse.success();
    }

    @PostMapping
    public void sendDataTest(@AuthUser User user) {
        sseService.notify(user.getId(), "data");
    }
}
