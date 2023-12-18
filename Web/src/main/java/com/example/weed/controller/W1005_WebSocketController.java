package com.example.weed.controller;

import com.example.weed.adapter.W1005_LocalDateTimeTypeAdapter;
import com.example.weed.entity.ChatMessage;
import com.example.weed.entity.Member;
import com.example.weed.repository.W1005_ChatMessageRepository;
import com.example.weed.service.W1001_MemberService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Controller
public class W1005_WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final W1005_ChatMessageRepository chatMessageRepository;
    private final W1001_MemberService memberService;
    private final Gson gson;

    @Autowired
    public W1005_WebSocketController(
            SimpMessagingTemplate messagingTemplate,
            W1005_ChatMessageRepository chatMessageRepository,
            W1001_MemberService memberService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageRepository = chatMessageRepository;
        this.memberService = memberService;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new W1005_LocalDateTimeTypeAdapter())
                .create();
    }

    @MessageMapping("/chat/{roomId}/sendMessage")
    public void sendMessage(
            @Payload String message,
            @DestinationVariable Long roomId,
            Authentication authentication
    ) {
        // 채팅 메시지를 받아서 처리하는 로직
        System.out.println("Received message in chat room " + roomId + ": " + message);

        // 현재 사용자의 정보 가져오기
        String username = authentication.getName();
        Member loggedInMember = memberService.W1001_getMemberInfo(username);


        // 메세지 DB에 저장
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId(roomId);
        chatMessage.setContent(message);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setMember(loggedInMember);

        chatMessageRepository.save(chatMessage);

        // 다시 해당 채팅방으로 메시지를 보내는 예시
        messagingTemplate.convertAndSend("/topic/chat/" + roomId + "/messages", message);
    }

    @MessageMapping("/chat/{roomId}/getMessages")
    public void getChatMessages(@DestinationVariable Long roomId, Model model) {
        // 채팅방에서 데이터베이스에서 저장된 메시지를 검색
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(roomId);
        Member member = memberService.getLoggedInMember();
        model.addAttribute("member",member);
        // 채팅방으로 메시지 전달
        for (ChatMessage chatMessage : chatMessages) {
            // ChatMessage 객체를 Gson을 사용하여 JSON 형태로 변환
            String jsonMessage = gson.toJson(chatMessage);
            messagingTemplate.convertAndSend("/topic/chat/" + roomId + "/messages", jsonMessage);
        }
    }

}
