package com.example.weed.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class W1006_ChatFileDTO {

    private Long id;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadTime;

    public W1006_ChatFileDTO(Long id, String fileName, String filePath, LocalDateTime uploadTime) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadTime = uploadTime;
    }

    // 새로운 생성자 추가: LocalDateTime을 받지 않고, 서버 현재 시간을 자동으로 할당하는 생성자
    public W1006_ChatFileDTO(Long id, String fileName, String filePath) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadTime = LocalDateTime.now();
    }
}
