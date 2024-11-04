package com.reviewping.coflo.global.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BatchJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job myJob;

    // TODO: 운영 시 주기 조정
    @Scheduled(cron = "0 50 23 * * SUN") // 매주 일요일 오후 11시 50분에 실행
    public void runVoteVerificationJob() throws Exception {
        JobParameters params =
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters();

        jobLauncher.run(myJob, params);
    }
}
