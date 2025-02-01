package com.jabadurai.archival;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

    private final CombineFilesTasklet combineFilesTasklet;
    private final MoveFilesTasklet moveFilesTasklet;

    @Bean
    public Job fileProcessingJob(JobRepository jobRepository, Step combineFilesStep, Step moveFilesStep) {
        log.info("Creating fileProcessingJob...");
        Job job = new JobBuilder("fileProcessingJob", jobRepository)
                .start(combineFilesStep)
                .next(moveFilesStep)
                .build();
        log.info("fileProcessingJob created.");
        return job;
    }

    @Bean
    public Step combineFilesStep(JobRepository jobRepository, PlatformTransactionManager   transactionManager) {
        log.info("Creating combineFilesStep...");
        Step step = new StepBuilder("combineFilesStep", jobRepository)
                .tasklet(combineFilesTasklet, transactionManager)
                .build();
        log.info("combineFilesStep created.");
        return step;
    }

    @Bean
    public Step moveFilesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("Creating moveFilesStep...");
        Step step = new StepBuilder("moveFilesStep", jobRepository)
                .tasklet(moveFilesTasklet, transactionManager)
                .build();
        log.info("moveFilesStep created.");
        return step;
    }

//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }

//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:testdb");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("");
//        return dataSource;
//    }

}