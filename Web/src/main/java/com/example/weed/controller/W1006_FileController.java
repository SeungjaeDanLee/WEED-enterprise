package com.example.weed.controller;

import com.example.weed.dto.W1006_FileUploadDTO;
import com.example.weed.entity.ChatFile;
import com.example.weed.entity.ChatMessage;
import com.example.weed.service.W1006_S3FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequestMapping("/s3")
@Slf4j
public class W1006_FileController {

    @Autowired
    private W1006_S3FileService fileService;

    // 프론트에서 ajax 를 통해 /upload 로 MultipartFile 형태로 파일과 roomId 를 전달받음
    // 전달받은 file 를 uploadFile 메서드를 통해 업로드
    @PostMapping("/upload")
    public W1006_FileUploadDTO uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("roomId") String roomId) {

        W1006_FileUploadDTO fileReq = fileService.uploadFile(file, UUID.randomUUID().toString(), roomId);
        log.info("최종 upload Data {}", fileReq);

        // fileReq 객체 리턴
        return fileReq;
    }


    // get 으로 요청이 오면 아래 download 메서드를 실행
    // fileName 과 파라미터로 넘어온 fileDir 을 getObject 메서드에 매개변수로 넣음
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> download(@PathVariable String fileName, @RequestParam("fileDir") String fileDir) {

        log.info("fileDir : fileName [{} : {}]", fileDir, fileName);
        try {
            // 변환된 byte, httpHeader 와 HttpStatus 가 포함된 ResponseEntity 객체를 return
            return fileService.getObject(fileDir, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
