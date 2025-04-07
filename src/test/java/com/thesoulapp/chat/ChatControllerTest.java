package com.thesoulapp.chat;

import com.thesoulapp.chat.controller.ChatController;
import com.thesoulapp.chat.model.ChatMessage;
import com.thesoulapp.chat.repository.ChatMessageRepository;
import com.thesoulapp.user.model.User;
import com.thesoulapp.user.model.UserRole;
import com.thesoulapp.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ChatControllerTest {

    @Autowired
    private ChatController chatController;

    @MockBean
    private ChatMessageRepository chatMessageRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    private User sender;
    private User receiver;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(1L);
        sender.setEmail("sender@test.com");
        sender.setRole(UserRole.PATIENT);

        receiver = new User();
        receiver.setId(2L);
        receiver.setEmail("therapist@test.com");
        receiver.setRole(UserRole.THERAPIST);

        authentication = new UsernamePasswordAuthenticationToken(sender.getEmail(), null);

        when(userRepository.findByEmail(sender.getEmail())).thenReturn(Optional.of(sender));
    }

    @Test
    void sendMessage_ShouldSaveAndForwardMessage() {
        // Arrange
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setReceiver(receiver);
        chatMessage.setContent("Test message");

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        // Act
        chatController.sendMessage(chatMessage, authentication);

        // Assert
        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(messagingTemplate).convertAndSendToUser(
            eq(receiver.getEmail()),
            eq("/queue/messages"),
            any(ChatMessage.class)
        );
    }

    @Test
    void markAsRead_ShouldUpdateMessageAndNotifySender() {
        // Arrange
        ChatMessage message = new ChatMessage();
        message.setId(1L);
        message.setSender(receiver);
        message.setReceiver(sender);
        message.setContent("Test message");

        when(chatMessageRepository.findById(1L)).thenReturn(Optional.of(message));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(message);

        // Act
        chatController.markAsRead(1L, authentication);

        // Assert
        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(messagingTemplate).convertAndSendToUser(
            eq(receiver.getEmail()),
            eq("/queue/read-receipts"),
            eq(1L)
        );
    }
} 