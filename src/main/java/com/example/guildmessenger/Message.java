package com.example.guildmessenger;

import javax.persistence.*;

@Entity
@Table(name = "message")
public class Message {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

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

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "created_date", nullable = false, updatable = false)
    private long createdDate;

    @Column(name = "sender_id", nullable = false, length = 30)
    private String senderId;

    @Column(name = "recipient_id", nullable = false, length = 30)
    private String recipientId;

    @Column(name = "message_text", nullable = false, length = 120)
    String messageText;
}
