package com.example.weed.service;

import com.example.weed.entity.File;
import com.example.weed.entity.Member;
import com.example.weed.repository.W1008_FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface W1008_FileService {


    File findByFileName(String fileName);

    void save(File file);

    List<File> uploadFile(MultipartFile[] uploadFiles, Member loggedInMember);

}
