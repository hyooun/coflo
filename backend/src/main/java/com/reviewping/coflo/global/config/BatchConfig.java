package com.reviewping.coflo.global.config;

import com.reviewping.coflo.global.batch.AiRewardTotalTasklet;
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
    public Job bestMergeRequestEventJob(
            JobRepository jobRepository,
            @Qualifier("addBestMrHistoryStep") Step addBestMrHistoryStep,
            @Qualifier("bestMergeRequestEventStep") Step bestMergeRequestEventStep,
            JobExecutionListener jobExecutionListener) {
        return new JobBuilder("bestMergeRequestJob", jobRepository)
                .listener(jobExecutionListener)
                .start(addBestMrHistoryStep)
                .next(bestMergeRequestEventStep)
                .build();
    }

    @Bean
    public Job aiRewardEventJob(
            JobRepository jobRepository,
            @Qualifier("aiRewardEventStep") Step aiRewardEventStep,
            JobExecutionListener jobExecutionListener) {
        return new JobBuilder("aiRewardEventJob", jobRepository)
                .listener(jobExecutionListener)
                .start(aiRewardEventStep)
                .build();
    }

    @Bean
    public Step addBestMrHistoryStep(
            JobRepository jobRepository,
            BestMergeRequestHistoryTasklet bestMergeRequestHistoryTasklet,
            PlatformTransactionManager transactionManager) {
        return new StepBuilder("addBestMrHistoryStep", jobRepository)
                .tasklet(bestMergeRequestHistoryTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step bestMergeRequestEventStep(
            JobRepository jobRepository,
            BestMergeRequestBadgeTasklet bestMergeRequestBadgeTasklet,
            PlatformTransactionManager transactionManager) {
        return new StepBuilder("bestMergeRequestEventStep", jobRepository)
                .tasklet(bestMergeRequestBadgeTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step aiRewardEventStep(
            JobRepository jobRepository,
            AiRewardTotalTasklet aiRewardTotalTasklet,
            PlatformTransactionManager transactionManager) {
        return new StepBuilder("aiRewardEventStep", jobRepository)
                .tasklet(aiRewardTotalTasklet, transactionManager)
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
