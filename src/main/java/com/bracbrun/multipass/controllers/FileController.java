package com.bracbrun.multipass.controllers;

import com.bracbrun.multipass.services.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("api/v1")
public class FileController {

    private final FileService _fileService;

    FileController(FileService fileService) {
        _fileService = fileService;
    }

    private static String getFileNameFromHeadersOrUUID(HttpServletRequest request) {
        val headerName = StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(
                                request.getHeaderNames().asIterator(), Spliterator.ORDERED), false)
                .filter(header -> header.matches("(file|file.*name)"))
                .findFirst()
                .orElse(UUID.randomUUID().toString());
        val potentialFileName = request.getHeader(headerName);
        return potentialFileName == null ? headerName : potentialFileName;
    }

    @GetMapping(value = "file/{fileName}")
    public ResponseEntity<Integer> processAndReturnFiles(@PathVariable String fileName, @RequestParam(defaultValue = "true") boolean synced) {
        val result = _fileService.processFile(fileName, synced);

        return ResponseEntity.ok(result);
    }

    @RequestMapping(path = "file", method = RequestMethod.POST, consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<String> uploadFile(HttpServletRequest request) throws IOException {
        try {
            _fileService.uploadFile(getFileNameFromHeadersOrUUID(request), request.getInputStream().readAllBytes());
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
