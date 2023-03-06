package com.bracbrun.multipass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: Add some capability of pulling files from S3
// TODO: Add capability of pushing those files into DB
// TODO: Add capability of multi-threaded processing of those files
@SpringBootApplication
public class MultiPassApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiPassApplication.class, args);
    }

}
