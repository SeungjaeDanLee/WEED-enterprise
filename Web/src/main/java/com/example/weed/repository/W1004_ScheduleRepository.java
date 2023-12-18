package com.example.weed.repository;

import com.example.weed.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface W1004_ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 모든 멤버의 일정 가져오기
    List<Schedule> findAll();

}
