package com.thesoulapp.web.chat;

import com.thesoulapp.application.chat.ChatService;
import com.thesoulapp.domain.chat.Message;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.UUID;

@Controller
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/chat/{roomId}")
    public MessageResponse sendMessage(@Payload ChatMessageRequest request, SimpMessageHeaderAccessor headerAccessor) {
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        String role = (String) headerAccessor.getSessionAttributes().get("role");
        // Only allow if user is sender or recipient
        if (!request.senderId().equals(userId) && !request.recipientId().equals(userId)) {
            throw new SecurityException("Not allowed");
        }
        Message message = Message.builder()
                .roomId(request.roomId())
                .senderId(UUID.fromString(request.senderId()))
                .recipientId(UUID.fromString(request.recipientId()))
                .content(request.content())
                .timestamp(Instant.now())
                .build();
        Message saved = chatService.saveMessage(message);
        return MessageResponse.from(saved);
    }

    @Builder
    public record ChatMessageRequest(String roomId, String senderId, String recipientId, String content) {}
    @Builder
    public record MessageResponse(String id, String roomId, String senderId, String recipientId, String content, Instant timestamp) {
        public static MessageResponse from(Message m) {
            return MessageResponse.builder()
                    .id(m.getId().toString())
                    .roomId(m.getRoomId())
                    .senderId(m.getSenderId().toString())
                    .recipientId(m.getRecipientId().toString())
                    .content(m.getContent())
                    .timestamp(m.getTimestamp())
                    .build();
        }
    }
} 