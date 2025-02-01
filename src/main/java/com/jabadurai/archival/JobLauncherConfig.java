package com.jabadurai.archival;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobLauncherConfig {

    private final JobLauncher jobLauncher;
    private final Job fileProcessingJob;

    @Bean
    public CommandLineRunner runJob() {
        return args -> {
            log.info("Starting fileProcessingJob...");
            jobLauncher.run(fileProcessingJob, new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters());
            log.info("fileProcessingJob started.");
        };
    }
}