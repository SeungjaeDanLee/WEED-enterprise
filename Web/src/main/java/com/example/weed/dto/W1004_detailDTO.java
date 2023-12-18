package com.example.weed.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class W1004_detailDTO {
    private Long scheduleId;
    private Long memberId;
    private String memberName;


    private String title;
    private String content;
    private String color;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    // 기본 생성자
    public W1004_detailDTO() {
    }

    public W1004_detailDTO(Long scheduleId, Long memberId, String memberName, String title, String content, String color, Date startDate, Date endDate) {
        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.title = title;
        this.content = content;
        this.color = color;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
