package com.thesoulapp.application.chat;

import com.thesoulapp.domain.chat.Message;
import java.util.List;

public interface ChatService {
    Message saveMessage(Message message);
    List<Message> getMessages(String roomId);
} 