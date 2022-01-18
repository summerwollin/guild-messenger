package com.example.guildmessenger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MessageDao {
    @Autowired
    private MessageRepository repository;

    public Message saveMessage(MessageRequest request) {
        Message message = new Message();
        message.setSenderId(request.getSenderId());
        message.setRecipientId(request.getRecipientId());
        message.setMessageText(request.getMessageText());
        message.setCreatedDate(System.currentTimeMillis());
        return repository.save(message);
    }
}
