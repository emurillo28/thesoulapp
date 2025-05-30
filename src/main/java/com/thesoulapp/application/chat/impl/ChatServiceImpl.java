package com.thesoulapp.application.chat.impl;

import com.thesoulapp.application.chat.ChatService;
import com.thesoulapp.domain.chat.Message;
import com.thesoulapp.domain.chat.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    private final MessageRepository messageRepository;

    @Autowired
    public ChatServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessages(String roomId) {
        return messageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }
} 