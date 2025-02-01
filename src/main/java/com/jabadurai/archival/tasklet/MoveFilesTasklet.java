package com.jabadurai.archival.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
@Slf4j
public class MoveFilesTasklet implements Tasklet {

    @Value("${temp.directory}")
    String tempDir;

    @Value("${target1.directory}")
    String targetDir1;

    @Value("${target2.directory}")
    String targetDir2;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Starting MoveFilesTasklet...");

        File tmpFolder = new File(tempDir);
        File[] files = tmpFolder.listFiles((dir, name) -> name.startsWith("combined_"));

        if (files == null || files.length == 0) {
            log.info("No files found in the temp directory.");
            return RepeatStatus.FINISHED;
        }

        for (File file : files) {
            log.info("Moving file: {}", file.getName());
            Files.copy(file.toPath(), Paths.get(targetDir1, file.getName()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(file.toPath(), Paths.get(targetDir2, file.getName()), StandardCopyOption.REPLACE_EXISTING);
            log.info("Deleting file from temp directory: {}", file.getName());
            file.delete(); // Delete from temp directory after moving
        }

        log.info("MoveFilesTasklet finished.");
        return RepeatStatus.FINISHED;
    }
}