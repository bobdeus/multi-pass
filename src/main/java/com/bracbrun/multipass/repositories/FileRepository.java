package com.bracbrun.multipass.repositories;

import com.bracbrun.multipass.models.MyS3File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<MyS3File, Long> {
    List<MyS3File> findByFileName(String fileName);
}
