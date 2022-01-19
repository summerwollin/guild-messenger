package com.example.guildmessenger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Spring Data JPA can construct SQL queries from method names if the method name matches their predefined syntax.
// You can find the list of available method names in the docs:
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
// Since this query is complex I opted for writing out the raw SQL instead of using a lengthy method name for readability concerns

public interface MessageRepository extends JpaRepository<Message, Long> {
    // Find up to 100 messages for a given sender and recipient that were created after the specified date
    @Query(value ="""
            SELECT * FROM MESSAGES m
            WHERE m.sender_id = :sender
            AND m.recipient_id = :recipient
            AND m.created_date > :date
            ORDER BY created_date DESC
            LIMIT 100""",
            nativeQuery = true)
    List<Message> findMessagesForSenderAndRecipient(
            @Param("sender") String senderId, @Param("recipient") String recipientId, @Param("date") Long createdDate
    );
}
