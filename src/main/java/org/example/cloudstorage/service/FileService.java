package org.example.cloudstorage.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.example.cloudstorage.model.User;
import org.example.cloudstorage.model.File;
import org.example.cloudstorage.exception.FileStorageException;
import org.example.cloudstorage.exception.UserNotFoundException;
import org.example.cloudstorage.repository.UserRepository;
import org.example.cloudstorage.repository.FileRepository;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


@Service
public class FileService {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    public FileService(UserRepository userRepository, FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    public List<File> getFilesByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return fileRepository.findByUserId(user.getId());
    }

    public void uploadFile(Long userId, MultipartFile multipartFile) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        try {
            // Сохраняем файл на диск
            String filename = multipartFile.getOriginalFilename();
            String filepath = "/uploads/" + filename;
            java.io.File file = new java.io.File(filepath);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(multipartFile.getBytes());
            outputStream.close();

            // Сохраняем информацию о файле в базу данных
            File fileEntity = new File(filepath);
            fileEntity.setFilename(filename);
            fileEntity.setFilepath(filepath);
            fileEntity.setUserId(userId);
            fileRepository.save(fileEntity);
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file: " + multipartFile.getOriginalFilename(), e);
        }
    }

    public void deleteFile(Long userId, String fileName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Находим файл по имени и пользователю
        File file = null;
        try {
            file = fileRepository.findByFilenameAndUserId(fileName, userId).orElseThrow(() -> new FileNotFoundException("File not found: " + fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Удаляем файл из базы данных и файловой системы
        fileRepository.delete(file);
        java.io.File fileOnDisk = new java.io.File(file.getFilepath());
        if (fileOnDisk.delete()) {
            System.out.println("File deleted successfully");
        } else {
            System.out.println("Failed to delete the file");
        }
    }
}
