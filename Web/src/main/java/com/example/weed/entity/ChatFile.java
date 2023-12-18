package com.example.weed.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ChatFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;
    private LocalDateTime uploadTime;

    @OneToOne(mappedBy = "chatFile", fetch = FetchType.LAZY)
    private ChatMessage chatMessage;

    @Builder
    public ChatFile(String fileName, String filePath, LocalDateTime uploadTime) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadTime = uploadTime;
    }
}
