package com.example.weed.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.weed.entity.File;
import com.example.weed.entity.Member;
import com.example.weed.repository.W1008_FileRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@NoArgsConstructor
public class W1008FileServiceImpl implements W1008_FileService {

    @Autowired
    private W1008_FileRepository w1008FileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String s3BucketName;

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public File findByFileName(String fileName) {
        return w1008FileRepository.findByFileName(fileName);
    }

    @Override
    public void save(File file) {
        w1008FileRepository.save(file);
    }

    @Override
    @Transactional
    public List<File> uploadFile(MultipartFile[] uploadFiles, Member loggedInMember) {
        for (MultipartFile uploadFile : uploadFiles) {
            if (!uploadFile.getContentType().startsWith("image")) {
                return null;
            }

            String originalName = uploadFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "_" + Paths.get(originalName).getFileName().toString();

            try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(uploadFile.getContentType());

                // 파일을 S3에 업로드
                PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, fileName, uploadFile.getInputStream(), metadata);
                amazonS3.putObject(putObjectRequest);

                // 업로드된 파일의 URL을 생성하고 파일 엔터티에 저장
                String fileUrl = amazonS3.getUrl(s3BucketName, fileName).toString();

                File existingFile = findByFileName(fileName);
                File memberId = w1008FileRepository.findByMemberId(loggedInMember.getId());

                if (loggedInMember.getFile() == null || memberId == null) {
                    File newFile = new File();
                    newFile.setFileName(fileName);
                    newFile.setFilePath(fileUrl);
                    newFile.setMember(loggedInMember);
                    if (loggedInMember.getId() != null) {
                        newFile.setId(loggedInMember.getId());
                    }

                    save(newFile);
                } else {
                    memberId.setFilePath(fileUrl);
                    memberId.setFileName(fileName);
                    save(memberId);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
