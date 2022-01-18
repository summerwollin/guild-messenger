package com.example.guildmessenger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// The service layer is where we write any business logic for processing the requests before calling into the DAO/repository.
// Since this is a simple app there is not much business logic in here yet,
// but it is still best practice to set up this separation concerns and future-proof the components.

@Service
public class MessageService {
    @Autowired
    MessageDao dao;

    public Message createMessage(MessageRequest request) {
        return dao.saveMessage(request);
    }
}
