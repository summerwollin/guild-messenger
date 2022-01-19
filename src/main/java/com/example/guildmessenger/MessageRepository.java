package com.example.guildmessenger;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Spring Data JPA can construct SQL queries from method names if the method name matches their predefined syntax.
// You can find the list of available method names in the docs:
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation

public interface MessageRepository extends JpaRepository<Message, Long> {
    // Find up to 100 messages for a given sender and recipient that were created after the specified date
    List<Message> findFirst100BySenderIdAndRecipientIdAndCreatedDateAfter(String senderId, String recipientId, Long createdDate);
}
