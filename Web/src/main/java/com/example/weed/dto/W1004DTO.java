package com.example.weed.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class W1004DTO {
    private Long memberId;
    private Long eventId;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private String color;

    //기본생성자
    public W1004DTO(){

    }

    public W1004DTO(Long memberId,Long eventId, String title, String content, Date startDate, Date endDate, String color){
        this.memberId = memberId;
        this.eventId = eventId;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.color = color;
    }


}
