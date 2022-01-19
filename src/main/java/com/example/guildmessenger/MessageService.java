package com.example.guildmessenger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// The service layer is where we write any business logic for processing the requests before calling into the DAO/repository.
// Since this is a simple app there is not much business logic in here yet,
// but it is still best practice to set up these separations of concerns.

@Service
public class MessageService {
    @Autowired
    MessageDao dao;

    public List<Message> getMessages(String senderId, String recipientId) {
        // TODO: make the 30 day limit a TTL for the data in the db and delete expired fields from the db table
        //  One example of the above would be a cron job on a regular schedule that runs automatically
        long thirtyDaysInMillis = 2592000000L;
        long timeLimit = System.currentTimeMillis() - thirtyDaysInMillis;
        return dao.findBySenderIdAndRecipientId(senderId, recipientId, timeLimit);
    }

    public Message createMessage(MessageRequest request) {
        Message message = new Message();
        message.setSenderId(request.getSenderId());
        message.setRecipientId(request.getRecipientId());
        message.setMessageText(request.getMessageText());
        message.setCreatedDate(System.currentTimeMillis());
        return dao.saveMessage(message);
    }
}
