package com.bracbrun.multipass.services;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

public class MpProfileCredentialsProvider implements AwsCredentialsProvider {

    @Override
    public AwsCredentials resolveCredentials() {
        return new MyCredentials();
    }
}
