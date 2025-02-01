package com.jabadurai.archival.tasklet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

public class MoveFilesTaskletTest {

    @Mock
    private StepContribution stepContribution;

    @Mock
    private ChunkContext chunkContext;

    @InjectMocks
    private MoveFilesTasklet moveFilesTasklet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        moveFilesTasklet.tempDir = "src/test/resources/temp";
        moveFilesTasklet.targetDir1 = "src/test/resources/target1";
        moveFilesTasklet.targetDir2 = "src/test/resources/target2";
    }

    @Test
    public void testExecute() throws Exception {
        // Create test files
        new File(moveFilesTasklet.tempDir).mkdirs();
        new File(moveFilesTasklet.targetDir1).mkdirs();
        new File(moveFilesTasklet.targetDir2).mkdirs();
        Files.createFile(Paths.get(moveFilesTasklet.tempDir, "combined_test.txt"));

        moveFilesTasklet.execute(stepContribution, chunkContext);

        // Verify files are moved
        assert Files.exists(Paths.get(moveFilesTasklet.targetDir1, "combined_test.txt"));
        assert Files.exists(Paths.get(moveFilesTasklet.targetDir2, "combined_test.txt"));
        assert !Files.exists(Paths.get(moveFilesTasklet.tempDir, "combined_test.txt"));
    }
}