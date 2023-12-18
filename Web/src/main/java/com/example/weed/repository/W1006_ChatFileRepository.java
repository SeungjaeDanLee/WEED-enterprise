package com.example.weed.repository;

import com.example.weed.entity.ChatFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface W1006_ChatFileRepository extends JpaRepository<ChatFile, Long> {
    // 특별한 쿼리 메서드가 필요하다면 여기에 추가할 수 있습니다.
}