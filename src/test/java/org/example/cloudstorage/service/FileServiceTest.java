package org.example.cloudstorage.service;

import org.example.cloudstorage.exception.FileStorageException;
import org.example.cloudstorage.exception.UserNotFoundException;
import org.example.cloudstorage.model.File;
import org.example.cloudstorage.model.User;
import org.example.cloudstorage.repository.FileRepository;
import org.example.cloudstorage.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    public FileServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getFilesByUserId_ExistingUserId_ReturnsListOfFiles() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(fileRepository.findByUserId(1L)).thenReturn(List.of(new File(), new File()));

        assertEquals(2, fileService.getFilesByUserId(1L).size());
    }

    @Test
    public void getFilesByUserId_NonExistingUserId_ThrowsUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> fileService.getFilesByUserId(999L));
    }

    @Test
    public void uploadFile_ValidInput_FileUploadedAndSavedToDatabase() throws IOException {
        User user = new User();
        user.setId(1L);

        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getBytes()).thenReturn("test content".getBytes());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(fileRepository.save(any(File.class))).thenAnswer(invocation -> invocation.getArgument(0));

        fileService.uploadFile(1L, multipartFile);

        verify(fileRepository, times(1)).save(any(File.class));
    }

    @Test
    public void uploadFile_IOExceptionThrown_FileStorageExceptionThrown() throws IOException {
        User user = new User();
        user.setId(1L);

        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getBytes()).thenThrow(new IOException());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(FileStorageException.class, () -> fileService.uploadFile(1L, multipartFile));
    }

    @Test
    public void deleteFile_ValidInput_FileDeletedFromDatabaseAndFileSystem() throws IOException {
        User user = new User();
        user.setId(1L);

        File file = new File();
        file.setUserId(1L);
        file.setFilename("test.txt");
        file.setFilepath("/uploads/test.txt");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(fileRepository.findByFilenameAndUserId("test.txt", 1L)).thenReturn(Optional.of(file));

        fileService.deleteFile(1L, "test.txt");

        verify(fileRepository, times(1)).delete(any(File.class));
    }
}
