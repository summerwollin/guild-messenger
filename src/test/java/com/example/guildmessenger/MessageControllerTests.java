package com.example.guildmessenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageDao dao;

    @AfterEach
    public void cleanUp() throws Exception {
        dao.deleteAll();
    }

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/")).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello Guild!")));
    }

    @Test
    public void shouldCreateNewMessage() throws Exception {
        MessageRequest request = new MessageRequest();
        request.setSenderId("me");
        request.setRecipientId("you");
        request.setMessageText("my message text");

        final ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(request);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                        .content(requestJson).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.senderId").value("me"))
                .andExpect(jsonPath("$.recipientId").value("you"))
                .andExpect(jsonPath("$.messageText").value("my message text"))
                .andExpect(jsonPath("$.createdDate").exists());
    }

    @Test
    public void shouldReturnErrorsOnBadPostRequest() throws Exception {
        MessageRequest request = new MessageRequest();
        request.setRecipientId("a_recipient_id_cannot_be_more_than_thirty_characters_long");

        final ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(request);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                        .content(requestJson).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.senderId").value("cannot be null"))
                .andExpect(jsonPath("$.recipientId").value("size must be between 1 and 30"))
                .andExpect(jsonPath("$.messageText").value("cannot be null"));
    }

    @Test
    public void shouldGetMessage() throws Exception {
        createAndSaveNewMessage(System.currentTimeMillis(), "bender", "fry", "Of all the friends I've had, you're the first");

        this.mockMvc.perform(MockMvcRequestBuilders.get("/messages?senderId=bender&recipientId=fry"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldOnlyGet100Messages() throws Exception {
        for (int i = 0; i < 105; i++) {
            createAndSaveNewMessage(System.currentTimeMillis(), "bender", "fry", "Of all the friends I've had, you're the first");
        }

        this.mockMvc.perform(MockMvcRequestBuilders.get("/messages?senderId=bender&recipientId=fry"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(100)));
    }

    @Test
    public void shouldNotGetMessagesForDifferentRecipient() throws Exception {
        createAndSaveNewMessage(System.currentTimeMillis(), "bender", "zoidberg", "Of all the friends I've had, you're the first");

        this.mockMvc.perform(MockMvcRequestBuilders.get("/messages?senderId=bender&recipientId=fry"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldNotReturnMessagesOlderThanThirtyDaysAgo() throws Exception {
        long oldDate = 1579325167000L; // Sat, 18 Jan 2020 05:26:07 GMT
        createAndSaveNewMessage(oldDate, "bender", "fry", "Of all the friends I've had, you're the first");

        this.mockMvc.perform(MockMvcRequestBuilders.get("/messages?senderId=bender&recipientId=fry"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldReturnErrorsOnBadGetRequest() throws Exception {
        createAndSaveNewMessage(System.currentTimeMillis(), "bender", "fry", "Of all the friends I've had, you're the first");

        this.mockMvc.perform(MockMvcRequestBuilders.get("/messages?recipientId=fry"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Required request parameter 'senderId' for method parameter type String is not present")));
    }

    private void createAndSaveNewMessage(long createdDate, String senderId, String recipientId, String messageText) {
        Message message = new Message();
        message.setCreatedDate(createdDate);
        message.setSenderId(senderId);
        message.setRecipientId(recipientId);
        message.setMessageText(messageText);
        dao.saveMessage(message);
    }

    private static String convertToJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
