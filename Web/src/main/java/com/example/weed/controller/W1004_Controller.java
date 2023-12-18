package com.example.weed.controller;

import com.example.weed.dto.W1001_CustomDetails;
import com.example.weed.dto.W1004DTO;
import com.example.weed.dto.W1004EventDTO;
import com.example.weed.entity.Member;
import com.example.weed.entity.Schedule;
import com.example.weed.repository.W1004_ScheduleRepository;
import com.example.weed.service.W1001_MemberService;
import com.example.weed.service.W1004service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Controller
public class W1004_Controller {

    @Autowired
    private W1004_ScheduleRepository w1004ScheduleRepository;


    @Autowired
    private W1001_MemberService memberService;

    @Autowired
    private W1004service w1004service;


    @GetMapping("/calendar")
    public String calendar() {
        return "W1004";
    }

    //이벤트 등록
    @PostMapping("/saveW1004")
    @ResponseBody
    public String saveEvent(@RequestBody W1004DTO w1004dto) {
        Schedule entity = new Schedule();
        entity.setScheduleId(w1004dto.getEventId());
        entity.setScheduleTitle(w1004dto.getTitle());
        entity.setScheduleStart(w1004dto.getStartDate());

        // Date를 LocalDateTime으로 변환
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(w1004dto.getEndDate().toInstant(), ZoneId.systemDefault());

        // 엔드데이터의 시간을 23:59:59로 설정
        LocalDateTime endDateTime = endLocalDateTime.with(LocalTime.of(23, 59, 59));

        // LocalDateTime을 Timestamp로 변환
        Timestamp endTimestamp = Timestamp.valueOf(endDateTime);
        entity.setScheduleEnd(endTimestamp);

        entity.setScheduleColor(w1004dto.getColor());
        entity.setScheduleContent(w1004dto.getContent());

        Long loggedInMemberId = getLoggedInMemberId();
        Member loggedInMember = memberService.findById(loggedInMemberId);
        entity.setMember(loggedInMember);

        w1004ScheduleRepository.save(entity);

        return "success";
    }

    @GetMapping("/api/events")
    @ResponseBody
    public List<W1004EventDTO> getAllEvents() {
        List<W1004EventDTO> eventDTOs = w1004service.getAllEvents();

        // 현재 로그인된 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            W1001_CustomDetails customDetails = (W1001_CustomDetails) authentication.getPrincipal();
            Member loggedInMember = customDetails.getLoggedInMember();

            // 이벤트 정보에 현재 로그인된 사용자의 정보 추가 또는 이름 변경
            eventDTOs.forEach(eventDTO -> {
                if (loggedInMember != null && loggedInMember.getId().equals(eventDTO.getMemberId())) {
                    eventDTO.setMemberName("나");
                }
            });
        }

        return eventDTOs;
    }


    private Long getLoggedInMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof W1001_CustomDetails customDetails) {
                Member loggedInMember = customDetails.getLoggedInMember();
                if (loggedInMember != null) {
                    return loggedInMember.getId();
                }
            }
        }
        return null;
    }


}
