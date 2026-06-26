package com.coditas.frontline.repository;

import com.coditas.frontline.entity.FileData;
import com.coditas.frontline.entity.Ticket;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
    Optional<FileData> findByName(@Nullable String originalFilename);

    Optional<FileData> findByTicket(Ticket ticket);
}
