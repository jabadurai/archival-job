package com.jabadurai.archival.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CombineFilesTasklet implements Tasklet {

    @Value("${input.directory}")
    private String inputDir;

    @Value("${temp.directory}")
    private String tempDir;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Starting CombineFilesTasklet...");

        File folder = new File(inputDir);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            log.info("No files found in the input directory.");
            return RepeatStatus.FINISHED;
        }

        Map<String, List<File>> groupedFiles = new HashMap<>();

        // Group files by hour (assuming filename format: "file_yyyyMMdd_HH_mmss.txt")
        for (File file : files) {
            String filename = file.getName();
            String hourKey = filename.substring(10, 13); // Extracting HH

            groupedFiles.computeIfAbsent(hourKey, k -> new ArrayList<>()).add(file);
        }

        // Combine files with the same hour into one file
        for (Map.Entry<String, List<File>> entry : groupedFiles.entrySet()) {
            String hour = entry.getKey();
            File combinedFile = new File(tempDir + "combined_" + hour + ".txt");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(combinedFile, true))) {
                for (File file : entry.getValue()) {
                    log.info("Processing file: {}", file.getName());
                    List<String> lines = Files.readAllLines(file.toPath());
                    for (String line : lines) {
                        writer.write(line);
                        writer.newLine();
                    }
                    log.info("Deleting processed file: {}", file.getName());
                    file.delete(); // Delete processed file
                }
            }
            log.info("Created combined file: {}", combinedFile.getName());
        }

        log.info("CombineFilesTasklet finished.");
        return RepeatStatus.FINISHED;
    }
}