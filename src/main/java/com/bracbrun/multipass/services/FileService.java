package com.bracbrun.multipass.services;

import com.bracbrun.multipass.models.MyS3File;
import com.bracbrun.multipass.repositories.FileRepository;
import lombok.val;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.time.LocalDateTime;

@Service
public class FileService {

    private final FileRepository _fileRepository;

    FileService(FileRepository fileRepository) {
        _fileRepository = fileRepository;
    }

    private String putS3Object(S3Client s3, String objectKey, byte[] fileBytes) {
        try {
            PutObjectRequest putOb = PutObjectRequest.builder().bucket("test-bucket-multi-pass").key(objectKey).build();

            PutObjectResponse response = s3.putObject(putOb, RequestBody.fromBytes(fileBytes));
            return response.eTag().replaceAll("\"", "");

        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void uploadFile(String fileName, byte[] file) {
        ensureFileNameHasNotBeenUploadedAlready(fileName);
        Region region = Region.US_WEST_2;
        S3Client s3 = S3Client.builder().region(region).credentialsProvider(new MpProfileCredentialsProvider()).build();

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
}
