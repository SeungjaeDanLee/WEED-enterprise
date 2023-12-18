package com.example.weed.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class W1007_WorkingDTO {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String date;

    private String checkInTime;
    private String checkOutTime;
    private String memberName;
    private String deptName;


    public W1007_WorkingDTO(Long id, LocalDate date, LocalDateTime checkInTime, LocalDateTime checkOutTime, String memberName, String deptName){
        this.id = id;
        this.date = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // 날짜를 문자열로 변환
        this.checkInTime = checkInTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME); // 시간을 문자열로 변환
        this.checkOutTime = checkOutTime != null ? checkOutTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
        this.memberName = memberName;
        this.deptName = deptName;
    }

}
