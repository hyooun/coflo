package com.reviewping.coflo.global.batch;

import com.reviewping.coflo.domain.mergerequest.service.BestMrHistoryService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BestMergeBadgeTasklet implements Tasklet {

    private final BestMrHistoryService bestMrHistoryService;

    @Override
    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3600000L))
    public RepeatStatus execute(
            @Nonnull StepContribution contribution, @Nonnull ChunkContext chunkContext) {
        bestMrHistoryService.addBestMrHistory();
        return RepeatStatus.FINISHED;
    }
}
