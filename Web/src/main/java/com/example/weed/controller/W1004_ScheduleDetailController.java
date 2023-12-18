package com.example.weed.controller;

import com.example.weed.dto.W1004_detailDTO;
import com.example.weed.service.W1004service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
public class W1004_ScheduleDetailController {

    private final W1004service W1004service;


    public W1004_ScheduleDetailController(W1004service scheduleService) {
        this.W1004service = scheduleService;
    }

    //멤버정보가져오기
    @GetMapping("/{scheduleId}/info")
    public ResponseEntity<W1004_detailDTO> getScheduleInfo(@PathVariable Long scheduleId) {
        W1004_detailDTO scheduleInfo = W1004service.getScheduleInfo(scheduleId);

        if (scheduleInfo != null) {
            return ResponseEntity.ok(scheduleInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 스케줄 업데이트
    @PutMapping("/{scheduleId}/update")
    public ResponseEntity<String> updateSchedule(@PathVariable Long scheduleId, @RequestBody W1004_detailDTO updatedInfo) {
        try {
            W1004service.updateScheduleInfo(scheduleId, updatedInfo);
            return ResponseEntity.ok("Schedule updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid date format: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    //스케줄 삭제
    @DeleteMapping("/{scheduleId}/delete")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long scheduleId) {
        try {
            W1004service.deleteSchedule(scheduleId);
            return ResponseEntity.ok("Schedule deleted successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

}
