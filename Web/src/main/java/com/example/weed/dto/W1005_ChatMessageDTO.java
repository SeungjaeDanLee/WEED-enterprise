package com.example.weed.dto;

import com.example.weed.entity.Member;
import com.example.weed.service.W1001_MemberService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
@Data
public class W1005_ChatMessageDTO {
    private Long id;
    private String content;
    private Long memberId;
    private String memberName;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private LocalDateTime timestamp;

    private W1006_ChatFileDTO chatFile;


    // 생성자에 ChatFile을 받아서 ChatFileDTO로 변환하는 로직 추가
    public W1005_ChatMessageDTO(Long id, String content, Long memberId, String memberName, LocalDateTime timestamp, W1006_ChatFileDTO chatFile) {
        this.id = id;
        this.content = content;
        this.memberId = memberId;
        this.memberName = memberName;
        this.timestamp = timestamp;

        this.chatFile = chatFile; // 이미 ChatFileDTO를 반환하는 메서드를 사용했으므로 별도의 변환 작업이 필요 없습니다.

    }


    public W1005_ChatMessageDTO() {
    }

}
