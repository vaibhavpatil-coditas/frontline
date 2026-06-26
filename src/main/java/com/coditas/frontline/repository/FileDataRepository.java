package com.coditas.frontline.repository;

import com.coditas.frontline.entity.FileData;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
    Optional<FileData> findByName(@Nullable String originalFilename);
}
