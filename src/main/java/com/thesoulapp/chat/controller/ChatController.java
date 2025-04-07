package com.thesoulapp.chat.controller;

import com.thesoulapp.chat.model.ChatMessage;
import com.thesoulapp.chat.repository.ChatMessageRepository;
import com.thesoulapp.user.model.User;
import com.thesoulapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessage chatMessage, Authentication authentication) {
        User sender = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        chatMessage.setSender(sender);
        chatMessage.setSentAt(LocalDateTime.now());
        chatMessageRepository.save(chatMessage);

        messagingTemplate.convertAndSendToUser(
            chatMessage.getReceiver().getEmail(),
            "/queue/messages",
            chatMessage
        );
    }

    @MessageMapping("/chat.read")
    public void markAsRead(@Payload Long messageId, Authentication authentication) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        
        message.setReadAt(LocalDateTime.now());
        chatMessageRepository.save(message);

        messagingTemplate.convertAndSendToUser(
            message.getSender().getEmail(),
            "/queue/read-receipts",
            messageId
        );
    }
} 