package com.bracbrun.multipass.services;

import lombok.val;
import software.amazon.awssdk.auth.credentials.AwsCredentials;

public class MyCredentials implements AwsCredentials {
    @Override
    public String accessKeyId() throws RuntimeException {
        val accessKeyResult = System.getenv("AWS_ACCESS_KEY_ID");
        if (accessKeyResult == null) {
            throw new RuntimeException("No access key was found. Please create an \"AWS_ACCESS_KEY_ID\" in your environment variables.");
        }

        return accessKeyResult;
    }

    @Override
    public String secretAccessKey() throws RuntimeException {
        val secretKeyResult = System.getenv("AWS_SECRET_ACCESS_KEY");
        if (secretKeyResult == null) {
            throw new RuntimeException("No secret key was found. Please create an \"AWS_SECRET_ACCESS_KEY\" in your environment variables.");
        }

        return secretKeyResult;
    }
}
