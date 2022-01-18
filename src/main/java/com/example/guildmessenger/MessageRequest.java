package com.example.guildmessenger;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MessageRequest {
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @NotNull(message = "cannot be null")
    @Size(min = 1, max = 30)
    String senderId;

    @NotNull(message = "cannot be null")
    @Size(min = 1, max = 30)
    String recipientId;

    @NotNull(message = "cannot be null")
    @Size(min = 1, max = 120)
    String messageText;
}
