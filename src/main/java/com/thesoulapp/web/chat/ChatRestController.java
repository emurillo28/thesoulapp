package com.thesoulapp.web.chat;

import com.thesoulapp.application.chat.ChatService;
import com.thesoulapp.domain.chat.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "Endpoints for chat message history")
public class ChatRestController {
    private final ChatService chatService;

    @Autowired
    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    @Operation(summary = "Get message history for a chat room (participants only)")
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable String roomId, Authentication authentication) {
        String userId = authentication.getDetails().toString();
        List<MessageResponse> messages = chatService.getMessages(roomId).stream()
                .filter(m -> m.getSenderId().toString().equals(userId) || m.getRecipientId().toString().equals(userId))
                .map(MessageResponse::from)
                .toList();
        return ResponseEntity.ok(messages);
    }

    @Builder
    public record MessageResponse(String id, String roomId, String senderId, String recipientId, String content, String timestamp) {
        public static MessageResponse from(Message m) {
            return MessageResponse.builder()
                    .id(m.getId().toString())
                    .roomId(m.getRoomId())
                    .senderId(m.getSenderId().toString())
                    .recipientId(m.getRecipientId().toString())
                    .content(m.getContent())
                    .timestamp(m.getTimestamp().toString())
                    .build();
        }
    }
} 