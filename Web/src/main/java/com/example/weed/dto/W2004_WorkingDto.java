package com.example.weed.dto;

import com.example.weed.entity.File;
import com.example.weed.entity.Member;
import com.example.weed.entity.Working;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class W2004_WorkingDto {
    private Long id;
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    @JsonIgnore
    private Member member;

    public static W2004_WorkingDto mapWorkingToDto(Working working) {
        W2004_WorkingDto workingDto = new W2004_WorkingDto();
        workingDto.setId(working.getId());
        workingDto.setDate(working.getDate());
        workingDto.setCheckInTime(working.getCheckInTime() != null ? LocalDateTime.from(working.getCheckInTime()) : null);
        workingDto.setCheckOutTime(working.getCheckOutTime() != null ? LocalDateTime.from(working.getCheckOutTime()) : null);
        workingDto.setMember(working.getMember());
        return workingDto;
    }
}