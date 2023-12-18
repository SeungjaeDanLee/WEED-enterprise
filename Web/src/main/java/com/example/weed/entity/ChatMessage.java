package com.example.weed.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @OneToOne
    @JoinColumn(name = "chat_file_id")
    private ChatFile chatFile;

    @Builder(builderMethodName = "hiddenBuilder")
    public ChatMessage(Long id, String content, Member member, LocalDateTime timestamp, ChatRoom chatRoom, ChatFile chatFile) {
        this.id = id;
        this.content = content;
        this.member = member;
        this.timestamp = timestamp;
        this.chatRoom = chatRoom;
        this.chatFile = chatFile;
    }

    public static ChatMessageBuilder builder() {
        return hiddenBuilder();
    }

    public void setRoomId(Long roomId) {
        this.chatRoom = new ChatRoom();
        this.chatRoom.setId(roomId);
    }

    public void setMemberId(Long memberId) {
        this.member = new Member();
        this.member.setId(memberId);
    }
}
