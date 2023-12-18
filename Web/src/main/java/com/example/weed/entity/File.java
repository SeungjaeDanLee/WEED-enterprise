package com.example.weed.entity;

import lombok.Data;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@BatchSize(size = 10)
public class File {

    @Id
    @GeneratedValue
    private Long id;

    private String fileName;
    private String filePath;

//    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY)  // mappedBy 속성 추가
//    private List<Member> members;

    @OneToOne
    @JoinColumn(name = "member_id")  // 외래 키 지정
    private Member member;
}

