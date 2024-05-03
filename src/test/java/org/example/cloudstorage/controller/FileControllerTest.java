package org.example.cloudstorage.controller;

import org.example.cloudstorage.model.File;
import org.example.cloudstorage.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(FileController.class)
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    public void testGetFilesByUserId() throws Exception {
        Long userId = 1L;
        List<File> files = Arrays.asList(new File("file1.txt"), new File("file2.txt"));

        when(fileService.getFilesByUserId(userId)).thenReturn(files);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].filename").value("file1.txt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].filename").value("file2.txt"));
    }

    @Test
    public void testUploadFile() throws Exception {
        Long userId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/{userId}", userId)
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testDeleteFile() throws Exception {
        Long userId = 1L;
        String fileName = "test.txt";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/files/{userId}/{fileName}", userId, fileName))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
