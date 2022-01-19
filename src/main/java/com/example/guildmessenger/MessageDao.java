package com.example.guildmessenger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDao {
    @Autowired
    private MessageRepository repository;

    public void deleteAll() {
        repository.deleteAll();
    }

    public List<Message> findBySenderIdAndRecipientId(String senderId, String recipientId, Long timeLimit) {
        return repository.findFirst100BySenderIdAndRecipientIdAndCreatedDateAfter(senderId, recipientId, timeLimit);
    }

    public Message saveMessage(Message message) {
        return repository.save(message);
    }
}
