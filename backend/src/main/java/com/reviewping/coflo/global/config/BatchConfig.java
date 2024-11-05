package com.reviewping.coflo.global.config;

import com.reviewping.coflo.global.batch.BestMergeRequestBadgeTasklet;
import com.reviewping.coflo.global.batch.BestMergeRequestHistoryTasklet;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@EnableBatchProcessing
@EnableRetry
@RequiredArgsConstructor
public class BatchConfig {

    @Bean
    public Job myJob(
            JobRepository jobRepository,
            @Qualifier("addHistoryStep") Step addHistoryStep,
            @Qualifier("eventStep") Step eventStep,
            JobExecutionListener jobExecutionListener) {
        return new JobBuilder("myJob", jobRepository)
                .listener(jobExecutionListener)
                .start(addHistoryStep)
                .next(eventStep)
                .build();
    }

    @Bean
    public Step addHistoryStep(
            JobRepository jobRepository,
            BestMergeRequestHistoryTasklet bestMergeRequestHistoryTasklet,
            PlatformTransactionManager transactionManager) {
        return new StepBuilder("addHistoryStep", jobRepository)
                .tasklet(bestMergeRequestHistoryTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step eventStep(
            JobRepository jobRepository,
            BestMergeRequestBadgeTasklet bestMergeRequestBadgeTasklet,
            PlatformTransactionManager transactionManager) {
        return new StepBuilder("eventStep", jobRepository)
                .tasklet(bestMergeRequestBadgeTasklet, transactionManager)
                .build();
    }

    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            private long startTime;

            @Override
            public void beforeJob(@Nonnull JobExecution jobExecution) {
                startTime = System.currentTimeMillis();
            }

            @Override
            public void afterJob(@Nonnull JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    log.info(
                            "Job Execution Duration: {}ms", System.currentTimeMillis() - startTime);
                }
            }
        };
    }
}
