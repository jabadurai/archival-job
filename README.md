# Archival Job

## Purpose
The Archival Job is designed to process and manage files in a specific directory structure. The job consists of two main tasklets: CombineFilesTasklet and MoveFilesTasklet. These tasklets work together to combine files based on a specific criterion and then move the combined files to target directories.

## Approach
The job is implemented using Spring Batch, which provides a robust framework for batch processing. The job reads files from an input directory, combines them based on their hour of creation, and then moves the combined files to two target directories. The job uses an H2 in-memory database to manage batch metadata and ensure the job's state is maintained across executions.

## Configuration
The job configuration is defined in the `application.properties` file. Key properties include:
- `input.directory`: Directory where the input files are located.
- `temp.directory`: Temporary directory where combined files are stored.
- `target1.directory` and `target2.directory`: Target directories where the combined files are moved.
- `spring.batch.jdbc.initialize-schema`: Ensures the batch schema is initialized.
- `spring.batch.schema`: Specifies the custom schema file for batch tables.
- `spring.datasource.*`: Configures the H2 in-memory database.

## Tasklets

### CombineFilesTasklet
The CombineFilesTasklet reads files from the input directory, groups them by the hour of their creation, and combines the contents of files with the same hour into a single file. The combined files are stored in the temporary directory.

#### Key Steps:
1. Read files from the input directory.
2. Group files by the hour of their creation.
3. Combine the contents of files with the same hour into a single file.
4. Store the combined file in the temporary directory.
5. Delete the original files after processing.

### MoveFilesTasklet
The MoveFilesTasklet moves the combined files from the temporary directory to two target directories. After moving the files, it deletes them from the temporary directory.

#### Key Steps:
1. Read combined files from the temporary directory.
2. Move each file to the two target directories.
3. Delete the files from the temporary directory after moving.

## Running the Job
To run the job, ensure that the `application.properties` file is correctly configured and the necessary directories exist. Start the Spring Boot application, and the job will process the files as described.