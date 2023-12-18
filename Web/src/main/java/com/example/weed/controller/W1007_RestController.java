package com.example.weed.controller;

import com.example.weed.dto.W1007_WorkingDTO;
import com.example.weed.entity.Working;
import com.example.weed.service.W1007_WorkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/working")
public class W1007_RestController {

    private final W1007_WorkingService w1007_WorkingService; // 변경

    @Autowired
    public W1007_RestController(W1007_WorkingService w1007_WorkingService) {
        this.w1007_WorkingService = w1007_WorkingService;
    }

    @GetMapping("/list")
    public List<W1007_WorkingDTO> getWorkingList() {
        List<Working> workingList = w1007_WorkingService.getWorkingList();
        // Working 엔티티를 W1007_WorkingDTO로 변환
        return workingList.stream()
                .map(working -> new W1007_WorkingDTO(
                        working.getId(),
                        working.getDate(),
                        working.getCheckInTime(),
                        working.getCheckOutTime(),
                        working.getMember() != null ? working.getMember().getName() : null, // null 체크 추가
                        working.getDeptName()
                ))
                .collect(Collectors.toList());
    }

}
