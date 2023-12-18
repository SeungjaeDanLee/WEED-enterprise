package com.example.weed.service;

import com.example.weed.dto.W1005_ChatMessageDTO;
import com.example.weed.entity.ChatMessage;
import com.example.weed.entity.ChatRoom;
import com.example.weed.entity.Member;
import com.example.weed.repository.W1001_MemberRepository;
import com.example.weed.repository.W1005_ChatMessageRepository;
import com.example.weed.repository.W1005_ChatRoomRepository;
import com.example.weed.repository.W1006_ChatFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class W1005_ChatService {

    @Autowired
    private W1005_ChatRoomRepository chatRoomRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private W1001_MemberRepository memberRepository;

    @Autowired
    private W1005_ChatMessageRepository chatMessageRepository;

    @Autowired
    private W1006_ChatFileRepository chatFileRepository;

//    public void sendChatMessageToUser(Long userId, String messageContent) {
//        // 사용자 ID로 사용자를 찾아서 메시지를 생성
//        Member user = memberRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("Member not found with id: " + userId));
//
//        ChatMessage newMessage = ChatMessage.builder()
//                .content(messageContent)
//                .member(user)
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        // 메시지 전송
//        messagingTemplate.convertAndSendToUser(user.getName(), "/queue/messages", newMessage);
//    }

    public void sendChatMessageToChatRoom(Long chatRoomId, String messageContent) {
        // 채팅방 ID로 채팅방을 찾아서 메시지를 생성
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found with id: " + chatRoomId));

        // 채팅방에 속한 모든 사용자에게 메시지 전송
        chatRoom.getMembers().forEach(member -> {
            ChatMessage newMessage = ChatMessage.builder()
                    .content(messageContent)
                    .timestamp(LocalDateTime.now())
                    .chatRoom(chatRoom)
                    .member(member) // Set the member for the message
                    .build();

            // Save the message to the database
            chatMessageRepository.save(newMessage);

            // Send the message to the user through WebSocket
            messagingTemplate.convertAndSendToUser(member.getName(), "/queue/messages", newMessage);
        });
    }


    private ChatMessage convertToEntity(Long roomId, W1005_ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId(roomId);
        chatMessage.setContent(chatMessageDTO.getContent());
        chatMessage.setMemberId(chatMessageDTO.getMemberId());
        // 다른 필요한 필드들을 설정하십시오.
        return chatMessage;
    }

    public void saveChatMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }
}

