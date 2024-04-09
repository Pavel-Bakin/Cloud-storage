package org.example.cloudstorage.controller;

import org.example.cloudstorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getFilesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(fileService.getFilesByUserId(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> uploadFile(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        fileService.uploadFile(userId, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/{fileName}")
    public ResponseEntity<?> deleteFile(@PathVariable Long userId, @PathVariable String fileName) {
        fileService.deleteFile(userId, fileName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
