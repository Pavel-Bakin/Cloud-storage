package org.example.cloudstorage.repository;

import org.example.cloudstorage.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByUserId(Long userId);

    Optional<File> findByFilenameAndUserId(String filename, Long userId);
}
