package com.example.weed.repository;

import com.example.weed.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface W1008_FileRepository extends JpaRepository<File, Long> {

    File findByFileName(String fileName);

    File findByMemberId(Long id);
}
