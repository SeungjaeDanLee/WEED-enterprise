package com.example.weed.service;

import com.example.weed.dto.W1004EventDTO;
import com.example.weed.dto.W1004_detailDTO;
import com.example.weed.entity.Member;
import com.example.weed.entity.Schedule;
import com.example.weed.repository.W1004_ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class W1004service {
    @Autowired
    private W1001_MemberService memberService;

    @Autowired
    private W1004_ScheduleRepository w1004ScheduleRepository;

    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    @Transactional
    public List<W1004EventDTO> getAllEvents() {
        // 모든 멤버의 스케줄 가져오기
        List<Schedule> allSchedules = w1004ScheduleRepository.findAll();

        // W1004Entity를 W1004EventDTO로 변환
        return allSchedules.stream()
                .map(this::convertToW1004EventDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveSchedule(W1004EventDTO w1004EventDTO) {
        Member loggedInMember = memberService.getLoggedInMember();

        if (loggedInMember != null) {
            // W1004EventDTO를 W1004Entity로 변환
            Schedule schedule = convertToW1004Entity(w1004EventDTO);

            // 스케줄 엔터티에 멤버 설정
            schedule.setMember(loggedInMember);

            // 스케줄 저장
            w1004ScheduleRepository.save(schedule);
        } else {
            // 로그인되지 않은 경우에 대한 처리
            throw new IllegalStateException("로그인된 멤버가 없습니다.");
        }
    }

    // W1004Entity를 W1004EventDTO로 변환하는 메소드
    private W1004EventDTO convertToW1004EventDTO(Schedule schedule) {
        W1004EventDTO w1004EventDTO = new W1004EventDTO();
        w1004EventDTO.setId(schedule.getScheduleId().toString());
        w1004EventDTO.setTitle(schedule.getScheduleTitle());
        w1004EventDTO.setStart(schedule.getScheduleStart());
        w1004EventDTO.setEnd(schedule.getScheduleEnd());
        w1004EventDTO.setColor(schedule.getScheduleColor());
        w1004EventDTO.setContent(schedule.getScheduleContent());

        // 필요한 경우 추가적인 필드 설정

        return w1004EventDTO;
    }

    // W1004EventDTO를 W1004Entity로 변환하는 메소드
    private Schedule convertToW1004Entity(W1004EventDTO w1004EventDTO) {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(Long.parseLong(w1004EventDTO.getId()));
        schedule.setScheduleTitle(w1004EventDTO.getTitle());
        schedule.setScheduleStart(w1004EventDTO.getStart());
        schedule.setScheduleEnd(w1004EventDTO.getEnd());
        schedule.setScheduleColor(w1004EventDTO.getColor());
        schedule.setScheduleContent(w1004EventDTO.getContent());

        // 필요한 경우 추가적인 필드 설정

        return schedule;
    }

    public W1004_detailDTO getScheduleInfo(Long scheduleId) {
        // 스케줄 정보 조회
        Schedule schedule = w1004ScheduleRepository.findById(scheduleId).orElse(null);

        if (schedule != null) {
            // 스케줄에 대한 멤버 정보 조회
            Member member = schedule.getMember();
            String memberName = (member != null) ? member.getName() : null;

            // W1004_detailDTO에 정보 저장하여 반환
            return new W1004_detailDTO(
                    schedule.getScheduleId(),
                    (member != null) ? member.getId() : null,  // 수정
                    memberName,
                    schedule.getScheduleTitle(),
                    schedule.getScheduleContent(),
                    schedule.getScheduleColor(),
                    schedule.getScheduleStart(),  // 날짜문제수정
                    schedule.getScheduleEnd()  // 날짜문제수정
            );
        } else {
            return null;
        }
    }

    @Transactional
    public void updateScheduleInfo(Long scheduleId, W1004_detailDTO updatedInfo) {
        // 스케줄 정보 조회
        Schedule schedule = w1004ScheduleRepository.findById(scheduleId).orElse(null);
        System.out.println("ㄷㄷㄷㄷㄷㄷㄷ: " + scheduleId);

        if (schedule != null) {
            // 업데이트된 정보로 스케줄 엔터티 업데이트
            schedule.setScheduleTitle(updatedInfo.getTitle());
            schedule.setScheduleContent(updatedInfo.getContent());
            schedule.setScheduleStart(updatedInfo.getStartDate());

            // 종료일을 받아와서 LocalDateTime 및 Timestamp로 변환
            LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(updatedInfo.getEndDate().toInstant(), ZoneId.systemDefault());
            LocalDateTime endDateTime = endLocalDateTime.with(LocalTime.of(23, 59, 59));
            Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

            schedule.setScheduleEnd(endTimestamp);

            schedule.setScheduleColor(updatedInfo.getColor());

            // 데이터베이스에 업데이트된 스케줄 정보 저장
            w1004ScheduleRepository.save(schedule);
        } else {
            // 스케줄이 존재하지 않는 경우에 대한 처리
            throw new IllegalStateException("해당 ID의 스케줄이 존재하지 않습니다.");
        }
    }

    //스케줄삭제
    @Transactional
    public void deleteSchedule(Long scheduleId) {
        // 스케줄 정보 조회
        Schedule schedule = w1004ScheduleRepository.findById(scheduleId).orElse(null);

        // 스케줄이 존재하면 삭제
        if (schedule != null) {
            w1004ScheduleRepository.delete(schedule);
        } else {
            // 스케줄이 존재하지 않는 경우 예외 처리
            throw new IllegalStateException("해당 ID의 스케줄이 존재하지 않습니다.");
        }
    }
}