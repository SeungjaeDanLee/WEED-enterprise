package com.example.weed.controller;

import com.example.weed.dto.W1005_ChatMessageDTO;
import com.example.weed.dto.W1006_ChatFileDTO;
import com.example.weed.entity.ChatMessage;
import com.example.weed.entity.ChatRoom;
import com.example.weed.service.W1005_ChatRoomService;
import com.example.weed.service.W1005_ChatService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/api/chat/{roomId}/messages")
public class W1005_ChatApiController {

    @Autowired
    private W1005_ChatRoomService chatRoomService;

    @Autowired
    private W1005_ChatService chatService;

    @GetMapping
    public List<W1005_ChatMessageDTO> getChatMessages(@PathVariable Long roomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(roomId);
        List<W1005_ChatMessageDTO> messages = chatRoomService.getChatMessagesByChatRoom(chatRoom)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        System.out.println(messages);
        return messages;
    }

    @PostMapping
    public void sendChatMessage(@PathVariable Long roomId, @RequestParam String messageContent) {
        chatService.sendChatMessageToChatRoom(roomId, messageContent);
    }

    private W1005_ChatMessageDTO convertToDTO(ChatMessage chatMessage) {
        W1005_ChatMessageDTO dto = new W1005_ChatMessageDTO(
                chatMessage.getId(),
                chatMessage.getContent(),
                chatMessage.getMember().getId(),
                chatMessage.getMember().getName(),
                chatMessage.getTimestamp(),
                chatMessage.getChatFile() != null ? new W1006_ChatFileDTO(
                        chatMessage.getChatFile().getId(),
                        chatMessage.getChatFile().getFileName(),
                        chatMessage.getChatFile().getFilePath()
                ) : null
        );

        return dto;
    }
}