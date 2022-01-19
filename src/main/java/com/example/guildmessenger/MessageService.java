package com.example.guildmessenger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// The service layer is where we write any business logic for processing the requests before calling into the DAO/repository.
// Since this is a simple app there is not much business logic in here yet,
// but it is still best practice to set up these separations of concerns.

@Service
public class MessageService {
    @Autowired
    MessageDao dao;

    final static long THIRTY_DAYS_MILLIS = 3 * 24 * 60 * 60 * 1000;

    public List<MessageResponse> getMessages(String senderId, String recipientId) {
        // TODO: make the 30 day limit a TTL for the data in the db and delete expired fields from the db table
        //  One example of the above would be a cron job on a regular schedule that runs automatically
        long timeLimit = System.currentTimeMillis() - THIRTY_DAYS_MILLIS;
        List<Message> messages = dao.findBySenderIdAndRecipientId(senderId, recipientId, timeLimit);
        return messages.stream().map(m -> createMessageResponse(m)).collect(Collectors.toList());
    }

    public MessageResponse createMessage(MessageRequest request) {
        Message message = createMessageFromRequest(request);
        Message savedMessage = dao.saveMessage(message);
        return createMessageResponse(savedMessage);
    }

    private Message createMessageFromRequest(MessageRequest mRequest) {
        Message message = new Message();
        BeanUtils.copyProperties(mRequest, message);
        message.setCreatedDate(System.currentTimeMillis());
        return message;
    }

    private MessageResponse createMessageResponse(Message message) {
        MessageResponse mResponse = new MessageResponse();
        BeanUtils.copyProperties(message, mResponse);
        return mResponse;
    }
}
