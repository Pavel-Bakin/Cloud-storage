package org.example.cloudstorage.repository;

import org.example.cloudstorage.model.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @Test
    public void testFindByUserId() {
        Long userId = 1L;

        List<File> files = fileRepository.findByUserId(userId);

        assertThat(files).isNotNull();
        assertThat(files.size()).isEqualTo(0);
    }
}
