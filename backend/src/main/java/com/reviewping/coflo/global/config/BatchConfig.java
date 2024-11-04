package com.reviewping.coflo.global.config;

import com.reviewping.coflo.global.batch.BestMergeBadgeTasklet;
import com.reviewping.coflo.global.batch.BestMergeHistoryTasklet;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow; // Spring Batch의 Flow
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
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
            Flow parallelFlow,
            JobExecutionListener jobExecutionListener) {
        return new JobBuilder("myJob", jobRepository)
                .listener(jobExecutionListener)
                .start(parallelFlow)
                .end()
                .build();
    }

    @Bean
    public Step noticeStep(
            JobRepository jobRepository,
            BestMergeHistoryTasklet bestMergeHistoryTasklet,
            PlatformTransactionManager transactionManager) {
        return new StepBuilder("noticeStep", jobRepository)
                .tasklet(bestMergeHistoryTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step eventStep(
            JobRepository jobRepository,
            BestMergeBadgeTasklet bestMergeBadgeTasklet,
            PlatformTransactionManager transactionManager) {
        return new StepBuilder("eventStep", jobRepository)
                .tasklet(bestMergeBadgeTasklet, transactionManager)
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

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("taskExecutor");
    }

    @Bean
    public Flow parallelFlow(
            @Qualifier("noticeStep") Step noticeStep, @Qualifier("eventStep") Step eventStep) {
        return new FlowBuilder<Flow>("parallelFlow")
                .split(taskExecutor())
                .add(
                        new FlowBuilder<Flow>("flow1").start(noticeStep).build(),
                        new FlowBuilder<Flow>("flow2").start(eventStep).build())
                .build();
    }
}
