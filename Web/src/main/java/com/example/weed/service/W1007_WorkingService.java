package com.example.weed.service;

import com.example.weed.entity.Working;
import com.example.weed.repository.W1007_WorkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class W1007_WorkingService {

    private final W1007_WorkingRepository workRepository;

    @Autowired
    public W1007_WorkingService(W1007_WorkingRepository workRepository) {
        this.workRepository = workRepository;
    }

    public List<Working> getAllWorkings() {
        return workRepository.findAll();
    }

    public List<Working> getWorkingList() {
        return workRepository.findAll();
    }
}