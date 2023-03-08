package com.bracbrun.multipass.services;

import com.bracbrun.multipass.models.MyS3File;
import com.bracbrun.multipass.repositories.FileRepository;
import lombok.val;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class FileService {

    private static final String BUCKET_NAME = "test-bucket-multi-pass";
    private final FileRepository _fileRepository;
    private final ProcessingService _processingService;
    private final S3Client s3 = S3Client.builder().region(Region.US_WEST_2).credentialsProvider(new MpProfileCredentialsProvider()).build();

    FileService(FileRepository fileRepository, ProcessingService processingService) {
        _fileRepository = fileRepository;
        _processingService = processingService;
    }

    private String putS3Object(S3Client s3, String objectKey, byte[] fileBytes) {
        try {
            PutObjectRequest putOb = PutObjectRequest.builder().bucket(BUCKET_NAME).key(objectKey).build();

            PutObjectResponse response = s3.putObject(putOb, RequestBody.fromBytes(fileBytes));
            return response.eTag().replaceAll("\"", "");

        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void uploadFile(String fileName, byte[] file) {
        ensureFileNameHasNotBeenUploadedAlready(fileName);

        String result = putS3Object(s3, fileName, file);

        val myFile = new MyS3File();
        myFile.setFileName(fileName);
        myFile.setObjectId(result);
        myFile.setCreatedOn(LocalDateTime.now());
        _fileRepository.save(myFile);

        s3.close();
    }

    private void ensureFileNameHasNotBeenUploadedAlready(String fileName) {
        if (_fileRepository.findByFileName(fileName).stream().anyMatch(file -> file.getFileName().equalsIgnoreCase(fileName))) {
            throw new RuntimeException(String.format("The file %s has already been uploaded.", fileName));
        }
    }

    public int processFile(String fileName, boolean synced) {
        val objectRequest = GetObjectRequest.builder().bucket(BUCKET_NAME).key(fileName).build();
        val result = s3.getObject(objectRequest);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(result, UTF_8))) {
            val lines = reader.lines().toList();
            val allValues = lines.stream().map((line) -> {
                val values = Arrays.stream(line.split(","));
                return values.map(Integer::parseInt).toList();
            }).toList();

            if (synced)
                return _processingService.syncedComputeValue(allValues);
            else
                return _processingService.nonSyncedComputeValue(allValues);

        } catch (IOException | SdkClientException | AwsServiceException exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(String.format("Something went wrong when attempting to process the file: %s", fileName), exception);
        }
    }
}
