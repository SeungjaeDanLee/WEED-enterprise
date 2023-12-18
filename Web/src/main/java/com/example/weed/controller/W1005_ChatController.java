package com.example.weed.controller;

import com.example.weed.dto.W1005_ChatMessageDTO;
import com.example.weed.dto.W1006_ChatFileDTO;
import com.example.weed.entity.ChatMessage;
import com.example.weed.entity.ChatRoom;
import com.example.weed.entity.Member;
import com.example.weed.repository.W1001_MemberRepository;
import com.example.weed.repository.W1005_ChatMessageRepository;
import com.example.weed.service.W1001_MemberService;
import com.example.weed.service.W1005_ChatRoomService;
import com.example.weed.service.W1005_ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/chat")
public class W1005_ChatController {

    @Autowired
    private W1005_ChatRoomService chatRoomService;

    @Autowired
    private W1005_ChatService chatService;

    @Autowired
    private W1001_MemberRepository memberRepository;

    @Autowired
    private W1001_MemberService w1001_memberService;

    @Autowired
    private W1005_ChatMessageRepository chatMessageRepository;

    // 채팅 시작포인트(채팅방 목록과 새 채팅방을 만들 수 있다.)
    @GetMapping
    public String showChatRooms(Model model) {
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRooms();
        model.addAttribute("chatRooms", chatRooms);
        return "W1005_chatRooms";
    }

    // 채팅방에 들어가는 엔드포인트
    @GetMapping("/{roomId}")
    public String enterChatRoom(@PathVariable Long roomId, Model model) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(roomId);
        model.addAttribute("chatRoom", chatRoom);
        return "W1005_chat";
    }

    // 새로운 채팅방을 만들기 위한 폼을 보여주는 엔드포인트
    @GetMapping("/create")
    public String showCreateChatRoomForm(Model model) {
        // 사용자 목록을 가져와서 모델에 추가
        List<Member> allMembers = memberRepository.findAll();
        model.addAttribute("allMembers", allMembers);
        return "W1005_newChat";
    }

    // 사용자가 입력한 채팅방 이름을 받아서 실제로 채팅방을 생성하는 엔드포인트
    @PostMapping("/create")
    public String createChatRoom(@RequestParam String chatRoomName) {
        chatRoomService.createChatRoom(chatRoomName);
        return "redirect:/chat";
    }

    @PostMapping("/{roomId}/invite")
    public String inviteUserToChatRoom(@PathVariable Long roomId, @RequestParam Long userId) {
        chatRoomService.addUserToChatRoom(roomId, userId);
        return "redirect:/chat/{roomId}";
    }

    @GetMapping("/chat")
    public String showUserChatRooms(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = w1001_memberService.getLoggedInMember();
        String username = authentication.getName();

        Member user = memberRepository.findByName(username);
        Set<ChatRoom> userChatRooms = user.getChatRooms();

        model.addAttribute("chatRooms", userChatRooms);
        model.addAttribute("member", member);

        return "W1005_chatRooms";
    }

    // 특정 채팅방의 채팅 메시지 목록을 보여주는 엔드포인트
    @GetMapping("/{roomId}/messages")
    public String showChatMessages(@PathVariable Long roomId, Model model) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(roomId);
        List<W1005_ChatMessageDTO> chatMessagesDTO = chatRoomService.getChatMessagesByChatRoom(chatRoom)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        // 사용자 이름 가져오기
        for (W1005_ChatMessageDTO w1005_ChatMessageDTO: chatMessagesDTO) {
            Member member = chatRoomService.getMember(w1005_ChatMessageDTO.getMemberId());

            w1005_ChatMessageDTO.setMemberName(member.getName());
            chatMessagesDTO.add(w1005_ChatMessageDTO);
        }
        model.addAttribute("chatMessages", chatMessagesDTO);
        return "W1005_chat";
    }

    // 특정 채팅방에 새로운 채팅 메시지를 전송하는 엔드포인트
    @PostMapping("/{roomId}/messages")
    public String sendChatMessage(@PathVariable Long roomId, @RequestParam String messageContent) {
        chatService.sendChatMessageToChatRoom(roomId, messageContent);
        return "redirect:/chat/{roomId}/messages";
    }


    // ChatMessage를 DTO로 변환하는 메소드
    private W1005_ChatMessageDTO convertToDTO(ChatMessage chatMessage) {
        W1005_ChatMessageDTO dto = new W1005_ChatMessageDTO(
                chatMessage.getId(),
                chatMessage.getContent(),
                chatMessage.getMember().getId(),
                chatMessage.getMember().getName(),
                chatMessage.getTimestamp(),
                null  // 초기화

        );

        // chatMessage의 chatFile이 null이 아니면 ChatFileDTO를 생성하여 할당
        if (chatMessage.getChatFile() != null) {
            dto.setChatFile(new W1006_ChatFileDTO(
                    chatMessage.getChatFile().getId(),
                    chatMessage.getChatFile().getFileName(),
                    chatMessage.getChatFile().getFilePath(),
                    chatMessage.getChatFile().getUploadTime()
            ));
        }

        return dto;
    }

}