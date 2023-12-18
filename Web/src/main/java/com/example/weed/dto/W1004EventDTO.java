package com.example.weed.dto;

import antlr.debug.Event;
import com.example.weed.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
public class W1004EventDTO {
    private String id;
    private String title;
    private Date start;
    private Date end;
    private String color;
    private String content;
    private Long memberId; // 이벤트를 등록한 멤버의 아이디
    private String memberName; // 이벤트를 등록한 멤버의 이름

    public W1004EventDTO() {
        // 기본 생성자
    }

}
