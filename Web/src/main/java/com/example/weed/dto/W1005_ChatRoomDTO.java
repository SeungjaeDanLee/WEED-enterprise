package com.example.weed.dto;

import lombok.Data;

import java.util.List;

@Data
public class W1005_ChatRoomDTO {
    private Long id;
    private String name;
    private List<Long> memberIds;
    private List<W1005_ChatMessageDTO> messages;

    public W1005_ChatRoomDTO(Long id, String name, List<Long> memberIds, List<W1005_ChatMessageDTO> messages) {
        this.id = id;
        this.name = name;
        this.memberIds = memberIds;
        this.messages = messages;
    }

    public W1005_ChatRoomDTO() {
    }
}
