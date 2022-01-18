package com.example.guildmessenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/")).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Hello Guild!")));
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
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.senderId").value("me"))
                .andExpect(jsonPath("$.recipientId").value("you"))
                .andExpect(jsonPath("$.messageText").value("my message text"))
                .andExpect(jsonPath("$.id").exists())
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
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.senderId").value("cannot be null"))
                .andExpect(jsonPath("$.recipientId").value("size must be between 1 and 30"))
                .andExpect(jsonPath("$.messageText").value("cannot be null"));
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
