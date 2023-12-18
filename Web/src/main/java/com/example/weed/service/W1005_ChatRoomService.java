package com.example.weed.service;

import com.example.weed.entity.ChatMessage;
import com.example.weed.entity.ChatRoom;
import com.example.weed.entity.Member;
import com.example.weed.repository.W1001_MemberRepository;
import com.example.weed.repository.W1005_ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class W1005_ChatRoomService {

    @Autowired
    private W1005_ChatRoomRepository chatRoomRepository;

    @Autowired
    private W1001_MemberRepository memberRepository;

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom getChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found with id: " + roomId));
    }

    public List<ChatMessage> getChatMessagesByChatRoom(ChatRoom chatRoom) {
        return chatRoom.getMessages();
    }

    public ChatRoom createChatRoom(String roomName) {
        ChatRoom newChatRoom = new ChatRoom();
        newChatRoom.setName(roomName);
        return chatRoomRepository.save(newChatRoom);
    }

    @Transactional
    public void addUserToChatRoom(Long roomId, Long memberId) {
        ChatRoom chatRoom = getChatRoomById(roomId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));

        // 이미 채팅방에 속한 사용자인지 확인
        if (!isMemberInChatRoom(chatRoom, member)) {
            // 채팅방에 속한 사용자가 아니라면 추가
            chatRoom.getMembers().add(member);
            chatRoomRepository.save(chatRoom);

            // 채팅방에 속한 사용자의 목록에도 추가
            member.getChatRooms().add(chatRoom);
            memberRepository.save(member);
        }
    }


    private boolean isMemberInChatRoom(ChatRoom chatRoom, Member member) {
        return chatRoom.getMembers().contains(member);
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));
    }
}

