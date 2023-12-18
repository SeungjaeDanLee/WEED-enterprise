package com.example.weed.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


@Getter
@Setter
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long scheduleId;;  //스케줄 ID

    @NotBlank
    private String scheduleTitle; //스케줄 제목


    private Date scheduleStart; //시작날짜

    private Date scheduleEnd; //종료날짜

    private String scheduleColor; //스케줄 컬러

    private String scheduleContent; //스케줄 내용

    //멤버테이블 조인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //멤버이름
    // Transient 필드를 사용하여 회원 이름 저장
    @Transient
    public String getMemberName() {
        if (member != null) {
            return member.getName();
        }
        return null;
    }
}

