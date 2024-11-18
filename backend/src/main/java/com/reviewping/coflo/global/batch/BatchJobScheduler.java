package com.reviewping.coflo.global.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class BatchJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job bestMergeRequestEventJob;
    private final Job aiRewardEventJob;

    public BatchJobScheduler(
            JobLauncher jobLauncher,
            @Qualifier("bestMergeRequestEventJob") Job bestMergeRequestEventJob,
            @Qualifier("aiRewardEventJob") Job aiRewardEventJob) {
        this.jobLauncher = jobLauncher;
        this.bestMergeRequestEventJob = bestMergeRequestEventJob;
        this.aiRewardEventJob = aiRewardEventJob;
    }

    @Scheduled(cron = "0 55 23 * * SUN") // 매주 일요일 오후 11시 55분에 실행
    public void runBestMergeRequestJob() throws Exception {
        JobParameters params =
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters();

        jobLauncher.run(bestMergeRequestEventJob, params);
    }

    @Scheduled(cron = "0 5 0 * * MON") // 매주 월요일 오전 00시 05분에 실행
    public void runAiRewardEventJob() throws Exception {
        JobParameters params =
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters();

        jobLauncher.run(aiRewardEventJob, params);
    }
}
